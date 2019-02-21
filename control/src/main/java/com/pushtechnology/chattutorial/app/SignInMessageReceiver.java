/*******************************************************************************
 * Copyright (C) 2019 Push Technology Ltd.
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

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.client.features.control.topics.TopicControl;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.pushtechnology.diffusion.datatype.DataTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opens the session, creates the topic and adds the request handler for signing
 * in.
 *
 */
public final class SignInMessageReceiver {
        private static Logger logger = LoggerFactory.getLogger(SignInMessageReceiver.class);

        public static void listeningForMessages() {
                final Session session = Diffusion.sessions().principal("admin").password("password").noReconnection()
                                .open("ws://localhost:8080");

                // Create the topic.
                final TopicControl topicControl = session.feature(TopicControl.class);
                topicControl.addTopic("Demos/Chat/Channel", topicControl.newSpecification(TopicType.TIME_SERIES)
                                .withProperty(TopicSpecification.TIME_SERIES_EVENT_VALUE_TYPE,
                                                DataTypes.JSON_DATATYPE_NAME)
                                .withProperty(TopicSpecification.TIME_SERIES_SUBSCRIPTION_RANGE, "limit 20")
                                .withProperty(TopicSpecification.REMOVAL, "when this session closes"))
                                .thenAccept(result -> logger.info("Topic creation successful: {}", result))
                                .exceptionally((err) -> {
                                        logger.error("Topic creation failed.", err);
                                        return null;
                                });

                // Add the Request Handler and listen to messages.
                final MessagingControl messagingControl = session.feature(MessagingControl.class);
                messagingControl.addRequestHandler("Demos/Chat/Messages/ClientJoin", JSON.class, JSON.class,
                                new SignInHandler(session))
                                .thenAccept((ignored) -> logger.info("Listening in on requests."));
        }
}
