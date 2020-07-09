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
import com.hazelcast.logging.Logger;

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
 * Adds an entry listener for this cache. For the types of events that the listener
 * will be notified for, see the documentation of the type field of the Cache event below.
 */
@Generated("e5b08e560dc8ad6bcea41c7cda211aca")
public final class CacheAddEntryListenerCodec {
    //hex: 0x130100
    public static final int REQUEST_MESSAGE_TYPE = 1245440;
    //hex: 0x130101
    public static final int RESPONSE_MESSAGE_TYPE = 1245441;
    private static final int REQUEST_LOCAL_ONLY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_LOCAL_ONLY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET = EVENT_CACHE_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_INITIAL_FRAME_SIZE = EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x130102
    private static final int EVENT_CACHE_MESSAGE_TYPE = 1245442;

    private CacheAddEntryListenerCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the cache.
         */
        public java.lang.String name;

        /**
         * If true fires events that originated from this node only, otherwise fires all events
         */
        public boolean localOnly;
    }

    public static ClientMessage encodeRequest(java.lang.String name, boolean localOnly) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("Cache.AddEntryListener");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET, localOnly);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        return clientMessage;
    }

    public static CacheAddEntryListenerCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.localOnly = decodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        return request;
    }

    public static ClientMessage encodeResponse(java.util.UUID response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET, response);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

    /**
    * Registration id for the registered listener.
    */
    public static java.util.UUID decodeResponse(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        ClientMessage.Frame initialFrame = iterator.next();
        return decodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
    }

    public static ClientMessage encodeCacheEvent(int type, java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys, int completionId) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[EVENT_CACHE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_CACHE_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeInt(initialFrame.content, EVENT_CACHE_TYPE_FIELD_OFFSET, type);
        encodeInt(initialFrame.content, EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET, completionId);
        clientMessage.add(initialFrame);

        ListMultiFrameCodec.encode(clientMessage, keys, CacheEventDataCodec::encode);
        return clientMessage;
    }

    public abstract static class AbstractEventHandler {

        public void handle(ClientMessage clientMessage) {
            int messageType = clientMessage.getMessageType();
            ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
            if (messageType == EVENT_CACHE_MESSAGE_TYPE) {
                ClientMessage.Frame initialFrame = iterator.next();
                int type = decodeInt(initialFrame.content, EVENT_CACHE_TYPE_FIELD_OFFSET);
                int completionId = decodeInt(initialFrame.content, EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET);
                java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys = ListMultiFrameCodec.decode(iterator, CacheEventDataCodec::decode);
                handleCacheEvent(type, keys, completionId);
                return;
            }
            Logger.getLogger(super.getClass()).finest("Unknown message type received on event handler :" + messageType);
        }

        /**
         * @param type The type of the event. Possible values for the event are:
         *             CREATED(1): An event type indicating that the cache entry was created.
         *             UPDATED(2): An event type indicating that the cache entry was updated, i.e. a previous mapping existed.
         *             REMOVED(3): An event type indicating that the cache entry was removed.
         *             EXPIRED(4): An event type indicating that the cache entry has expired.
         *             EVICTED(5): An event type indicating that the cache entry has evicted.
         *             INVALIDATED(6): An event type indicating that the cache entry has invalidated for near cache invalidation.
         *             COMPLETED(7): An event type indicating that the cache operation has completed.
         *             EXPIRATION_TIME_UPDATED(8): An event type indicating that the expiration time of cache record has been updated
         *             PARTITION_LOST(9): An event type indicating that partition loss is detected in given cache with name
         * @param keys The keys of the entries in the cache.
         * @param completionId User generated id which shall be received as a field of the cache event upon completion of the
         *                     request in the cluster.
        */
        public abstract void handleCacheEvent(int type, java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys, int completionId);
    }
}
