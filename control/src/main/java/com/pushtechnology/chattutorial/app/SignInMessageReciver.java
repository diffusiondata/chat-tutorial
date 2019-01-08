/*******************************************************************************
 * Copyright (C) 2016 Push Technology Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.pushtechnology.chattutorial.app;

import java.io.Console;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.content.Content;
import com.pushtechnology.diffusion.client.features.Messaging;
import com.pushtechnology.diffusion.client.features.TimeSeries;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.features.TimeSeries.Event;
import com.pushtechnology.diffusion.client.features.Topics.ValueStream;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.client.features.control.topics.TopicControl;
import com.pushtechnology.diffusion.client.features.control.topics.TopicUpdateControl;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl.MessageHandler;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl.RequestHandler;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.session.SessionId;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.client.types.ReceiveContext;
import com.pushtechnology.diffusion.conversation.ResponseHandler;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.pushtechnology.diffusion.datatype.primitive.impl.StringDataTypeImpl;

import static com.pushtechnology.diffusion.datatype.DataTypes.STRING_DATATYPE_NAME;

public class SignInMessageReciver {
    public void AwaitMessages() {
        try {
            final Session session = Diffusion.sessions().principal("admin").password("password").noReconnection()
                    .open("ws://localhost:8080");

            final TopicControl topicControl = session.feature(TopicControl.class);

            topicControl.addTopic("Demos/Chat/Channel",
                    topicControl.newSpecification(TopicType.TIME_SERIES)
                            .withProperty(TopicSpecification.TIME_SERIES_EVENT_VALUE_TYPE, STRING_DATATYPE_NAME)
                            .withProperty(TopicSpecification.TIME_SERIES_SUBSCRIPTION_RANGE, "limit 20")
                            .withProperty(TopicSpecification.REMOVAL, "when this session closes"));

            final MessagingControl messagingControl = session.feature(MessagingControl.class);
            messagingControl.addRequestHandler("ClientJoin", JSON.class, JSON.class,  new SignInHandler());

        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
