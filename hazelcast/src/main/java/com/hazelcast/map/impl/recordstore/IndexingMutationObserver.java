/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map.impl.recordstore;

import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.map.impl.MapContainer;
import com.hazelcast.map.impl.StoreAdapter;
import com.hazelcast.map.impl.record.Record;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.query.impl.Index;
import com.hazelcast.query.impl.Indexes;
import com.hazelcast.query.impl.InternalIndex;
import com.hazelcast.query.impl.QueryableEntry;

import javax.annotation.Nonnull;

import static com.hazelcast.map.impl.record.Records.getValueOrCachedValue;

public class IndexingMutationObserver<R extends Record> implements MutationObserver<R> {

    private final int partitionId;
    private final Storage<Data, R> storage;
    private final MapContainer mapContainer;
    private final StoreAdapter storeAdapter;
    private final SerializationService ss;

    public IndexingMutationObserver(SerializationService ss, RecordStore recordStore) {
        this.partitionId = recordStore.getPartitionId();
        this.mapContainer = recordStore.getMapContainer();
        this.storeAdapter = new RecordStoreAdapter(recordStore);
        this.storage = recordStore.getStorage();
        this.ss = ss;
    }

    @Override
    public void onPutRecord(@Nonnull Data key, @Nonnull R record,
                            Object oldValue, boolean backup) {
        if (!backup) {
            saveIndex(record, oldValue, Index.OperationSource.USER);
        }
    }

    @Override
    public void onReplicationPutRecord(@Nonnull Data key, @Nonnull R record, boolean populateIndex) {
        if (populateIndex) {
            saveIndex(record, null, Index.OperationSource.SYSTEM);
        }
    }

    @Override
    public void onUpdateRecord(@Nonnull Data key, @Nonnull R record,
                               Object oldValue, Object newValue, boolean backup) {
        if (!backup) {
            saveIndex(record, oldValue, Index.OperationSource.USER);
        }
    }

    @Override
    public void onRemoveRecord(@Nonnull Data key, R record) {
        removeIndex(record, Index.OperationSource.USER);
    }

    @Override
    public void onEvictRecord(@Nonnull Data key, @Nonnull R record) {
        removeIndex(record, Index.OperationSource.USER);
    }

    @Override
    public void onLoadRecord(@Nonnull Data key, @Nonnull R record, boolean backup) {
        if (!backup) {
            saveIndex(record, null, Index.OperationSource.USER);
        }
    }

    @Override
    public void onReset() {
        clearGlobalIndexes(false);
        // Partitioned indexes are cleared in MapReplicationStateHolder
    }

    @Override
    public void onClear() {
        onReset();
    }

    @Override
    public void onDestroy(boolean isDuringShutdown, boolean internal) {
        clearGlobalIndexes(isDuringShutdown);
        clearPartitionedIndexes(true);
    }

    /**
     * Only indexed data will be removed, index info will stay.
     */
    private void clearGlobalIndexes(boolean destroy) {
        Indexes indexes = mapContainer.getIndexes(partitionId);
        if (indexes.isGlobal()) {
            if (destroy) {
                indexes.destroyIndexes();
            } else {
                if (indexes.haveAtLeastOneIndex()) {
                    // clears indexed data of this partition
                    // from shared global index.
                    fullScanLocalDataToClear(indexes);
                }
            }
        }
    }

    /**
     * Only indexed data will be removed, index info will stay.
     */
    private void clearPartitionedIndexes(boolean destroy) {
        Indexes indexes = mapContainer.getIndexes(partitionId);
        if (indexes.isGlobal()) {
            return;
        }

        if (destroy) {
            indexes.destroyIndexes();
        } else {
            indexes.clearAll();
        }
    }

    /**
     * Clears local data of this partition from global index by doing
     * partition full-scan.
     */
    private void fullScanLocalDataToClear(Indexes indexes) {
        InternalIndex[] indexesSnapshot = indexes.getIndexes();
        for (Record record : storage.values()) {
            Data key = record.getKey();
            Object value = getValueOrCachedValue(record, ss);
            indexes.removeEntry(key, value, Index.OperationSource.SYSTEM);
        }
        Indexes.markPartitionAsUnindexed(partitionId, indexesSnapshot);
    }

    private void saveIndex(Record record, Object oldValue,
                           Index.OperationSource operationSource) {
        Indexes indexes = mapContainer.getIndexes(partitionId);
        if (!indexes.haveAtLeastOneIndex()) {
            return;
        }

        QueryableEntry queryableEntry = mapContainer.newQueryEntry(record.getKey(),
                getValueOrCachedValue(record, ss));
        queryableEntry.setRecord(record);
        queryableEntry.setStoreAdapter(storeAdapter);

        indexes.putEntry(queryableEntry, oldValue, operationSource);
    }

    private void removeIndex(Record record,
                             Index.OperationSource operationSource) {
        Indexes indexes = mapContainer.getIndexes(partitionId);
        if (!indexes.haveAtLeastOneIndex()) {
            return;
        }

        indexes.removeEntry(record.getKey(),
                getValueOrCachedValue(record, ss), operationSource);
    }
}
