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
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.control.clients.ClientControl;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.datatype.json.JSON;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.pushtechnology.diffusion.client.session.Session;

public final class SignInHandler implements MessagingControl.RequestHandler<JSON, JSON> {
    private final Session session;
    private final Map<String, String> users;

    public SignInHandler(Session session) {
        this.session = session;
        users = new HashMap<String, String>(3);
        
        users.putIfAbsent("Push", "password2");
        users.putIfAbsent("Diffusion", "password3");
    }

    @Override
    public void onClose() {
        Logger.getGlobal().info("Closing Handler.");
    }

    @Override
    public void onError(ErrorReason errorReason) {
        Logger.getGlobal().severe(errorReason.toString());
    }

    @Override
    public void onRequest(JSON request, RequestContext context, Responder<JSON> responder) {
        try {
            // Mapping for the json object request.
            final CBORFactory factory = new CBORFactory();
            final ObjectMapper mapper = new ObjectMapper();
            final TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
            };

            final CBORParser parser = factory.createParser(request.asInputStream());
            final Map<String, String> map = mapper.readValue(parser, typeReference);
            String username = map.get("username");
            String password = map.get("password");

            if (users.containsKey(username) && users.get(username).equals(password)) {

                // with ClientControl we can change the role for the Session.
                final ClientControl clientControl = session.feature(ClientControl.class);

                // Diffusion role that is going to be assigned to sessions who pass
                // authentication.
                clientControl.changeRoles(context.getSessionId(), new HashSet<String>(),
                        new HashSet<String>(Arrays.asList("CHAT"))).thenAccept((ignored) -> {
                            responder.respond(Diffusion.dataTypes().json().fromJsonString("{ \"status\": \"OK\" }"));
                        }).exceptionally((err) -> {
                            Logger.getGlobal().warning("Changing Role failed:\n" + err.toString());
                            responder.respond(Diffusion.dataTypes().json().fromJsonString(
                                    "{ \"status\": \"Fail\", \"message\": \"Server Error while changing roles.\" }"));
                            return null;
                        });
            } else {
                responder.respond(Diffusion.dataTypes().json()
                        .fromJsonString("{ \"status\": \"Fail\", \"message\": \"Authentication Failed.\" }"));
            }
        } catch (IOException e) {
            Logger.getGlobal().severe(e.toString());
        }
    }
}