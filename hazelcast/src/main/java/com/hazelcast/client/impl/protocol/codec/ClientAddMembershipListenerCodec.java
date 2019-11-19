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
 * TODO DOC
 */
@Generated("3bfd1411203a2d903bafa9ed3cc6886a")
public final class ClientAddMembershipListenerCodec {
    //hex: 0x000300
    public static final int REQUEST_MESSAGE_TYPE = 768;
    //hex: 0x000301
    public static final int RESPONSE_MESSAGE_TYPE = 769;
    private static final int REQUEST_LOCAL_ONLY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_LOCAL_ONLY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_MEMBER_EVENT_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_MEMBER_INITIAL_FRAME_SIZE = EVENT_MEMBER_EVENT_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x000302
    private static final int EVENT_MEMBER_MESSAGE_TYPE = 770;
    private static final int EVENT_MEMBER_LIST_INITIAL_FRAME_SIZE = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x000303
    private static final int EVENT_MEMBER_LIST_MESSAGE_TYPE = 771;
    private static final int EVENT_MEMBER_ATTRIBUTE_CHANGE_OPERATION_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_MEMBER_ATTRIBUTE_CHANGE_INITIAL_FRAME_SIZE = EVENT_MEMBER_ATTRIBUTE_CHANGE_OPERATION_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x000304
    private static final int EVENT_MEMBER_ATTRIBUTE_CHANGE_MESSAGE_TYPE = 772;

    private ClientAddMembershipListenerCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * if true only master node sends events, otherwise all registered nodes send all membership
         * changes.
         */
        public boolean localOnly;
    }

    public static ClientMessage encodeRequest(boolean localOnly) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("Client.AddMembershipListener");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET, localOnly);
        clientMessage.add(initialFrame);
        return clientMessage;
    }

    public static ClientAddMembershipListenerCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.localOnly = decodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * Returns the registration id for the listener.
         */
        public java.util.UUID response;
    }

    public static ClientMessage encodeResponse(java.util.UUID response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET, response);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

    public static ClientAddMembershipListenerCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        response.response = decodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
        return response;
    }

    public static ClientMessage encodeMemberEvent(com.hazelcast.cluster.Member member, int eventType) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[EVENT_MEMBER_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_MEMBER_MESSAGE_TYPE);
        encodeInt(initialFrame.content, EVENT_MEMBER_EVENT_TYPE_FIELD_OFFSET, eventType);
        clientMessage.add(initialFrame);

        MemberCodec.encode(clientMessage, member);
        return clientMessage;
    }
    public static ClientMessage encodeMemberListEvent(java.util.Collection<com.hazelcast.cluster.Member> members) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[EVENT_MEMBER_LIST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_MEMBER_LIST_MESSAGE_TYPE);
        clientMessage.add(initialFrame);

        ListMultiFrameCodec.encode(clientMessage, members, MemberCodec::encode);
        return clientMessage;
    }
    public static ClientMessage encodeMemberAttributeChangeEvent(com.hazelcast.cluster.Member member, java.util.Collection<com.hazelcast.cluster.Member> members, java.lang.String key, int operationType, @Nullable java.lang.String value) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[EVENT_MEMBER_ATTRIBUTE_CHANGE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_MEMBER_ATTRIBUTE_CHANGE_MESSAGE_TYPE);
        encodeInt(initialFrame.content, EVENT_MEMBER_ATTRIBUTE_CHANGE_OPERATION_TYPE_FIELD_OFFSET, operationType);
        clientMessage.add(initialFrame);

        MemberCodec.encode(clientMessage, member);
        ListMultiFrameCodec.encode(clientMessage, members, MemberCodec::encode);
        StringCodec.encode(clientMessage, key);
        CodecUtil.encodeNullable(clientMessage, value, StringCodec::encode);
        return clientMessage;
    }

    public abstract static class AbstractEventHandler {

        public void handle(ClientMessage clientMessage) {
            int messageType = clientMessage.getMessageType();
            ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
            if (messageType == EVENT_MEMBER_MESSAGE_TYPE) {
                ClientMessage.Frame initialFrame = iterator.next();
                int eventType = decodeInt(initialFrame.content, EVENT_MEMBER_EVENT_TYPE_FIELD_OFFSET);
                com.hazelcast.cluster.Member member = MemberCodec.decode(iterator);
                handleMemberEvent(member, eventType);
                return;
            }
            if (messageType == EVENT_MEMBER_LIST_MESSAGE_TYPE) {
                //empty initial frame
                iterator.next();
                java.util.Collection<com.hazelcast.cluster.Member> members = ListMultiFrameCodec.decode(iterator, MemberCodec::decode);
                handleMemberListEvent(members);
                return;
            }
            if (messageType == EVENT_MEMBER_ATTRIBUTE_CHANGE_MESSAGE_TYPE) {
                ClientMessage.Frame initialFrame = iterator.next();
                int operationType = decodeInt(initialFrame.content, EVENT_MEMBER_ATTRIBUTE_CHANGE_OPERATION_TYPE_FIELD_OFFSET);
                com.hazelcast.cluster.Member member = MemberCodec.decode(iterator);
                java.util.Collection<com.hazelcast.cluster.Member> members = ListMultiFrameCodec.decode(iterator, MemberCodec::decode);
                java.lang.String key = StringCodec.decode(iterator);
                java.lang.String value = CodecUtil.decodeNullable(iterator, StringCodec::decode);
                handleMemberAttributeChangeEvent(member, members, key, operationType, value);
                return;
            }
            Logger.getLogger(super.getClass()).finest("Unknown message type received on event handler :" + messageType);
        }
        public abstract void handleMemberEvent(com.hazelcast.cluster.Member member, int eventType);
        public abstract void handleMemberListEvent(java.util.Collection<com.hazelcast.cluster.Member> members);
        public abstract void handleMemberAttributeChangeEvent(com.hazelcast.cluster.Member member, java.util.Collection<com.hazelcast.cluster.Member> members, java.lang.String key, int operationType, @Nullable java.lang.String value);
    }
}
