/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.Generated;
import com.hazelcast.client.impl.protocol.codec.builtin.*;
import com.hazelcast.client.impl.protocol.codec.custom.*;

import javax.annotation.Nullable;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/*
 * This file is auto-generated by the Hazelcast Client Protocol Code Generator.
 * To change this file, edit the templates or the protocol
 * definitions on the https://github.com/hazelcast/hazelcast-client-protocol
 * and regenerate it.
 */

/**
 * Removes entries for the specified keys. The order in which the individual entries are removed is undefined.
 * For every entry in the key set, the following are called: any registered CacheEntryRemovedListeners if the cache
 * is a write-through cache, the CacheWriter. If the key set is empty, the CacheWriter is not called.
 */
@Generated("e54765ed927f35a895b647cca3b2cf6e")
public final class CacheRemoveAllKeysCodec {
    //hex: 0x130300
    public static final int REQUEST_MESSAGE_TYPE = 1245952;
    //hex: 0x130301
    public static final int RESPONSE_MESSAGE_TYPE = 1245953;
    private static final int REQUEST_COMPLETION_ID_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_COMPLETION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;

    private CacheRemoveAllKeysCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the cache.
         */
        public java.lang.String name;

        /**
         * The keys to remove.
         */
        public java.util.List<com.hazelcast.internal.serialization.Data> keys;

        /**
         * User generated id which shall be received as a field of the cache event upon completion of
         * the request in the cluster.
         */
        public int completionId;
    }

    public static ClientMessage encodeRequest(java.lang.String name, java.util.Collection<com.hazelcast.internal.serialization.Data> keys, int completionId) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("Cache.RemoveAllKeys");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeInt(initialFrame.content, REQUEST_COMPLETION_ID_FIELD_OFFSET, completionId);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        ListMultiFrameCodec.encode(clientMessage, keys, DataCodec::encode);
        return clientMessage;
    }

    public static CacheRemoveAllKeysCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.completionId = decodeInt(initialFrame.content, REQUEST_COMPLETION_ID_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        request.keys = ListMultiFrameCodec.decode(iterator, DataCodec::decode);
        return request;
    }

    public static ClientMessage encodeResponse() {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.add(initialFrame);

        return clientMessage;
    }


}
