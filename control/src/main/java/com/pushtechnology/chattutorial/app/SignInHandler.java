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

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.control.clients.ClientControl;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.datatype.json.JSON;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.pushtechnology.diffusion.client.session.Session;

public class SignInHandler implements MessagingControl.RequestHandler<JSON, JSON> {
    private Session _session;
    private final Hashtable<String, String> _users;

    public SignInHandler(Session session) {
        super();
        this._session = session;

        _users = new Hashtable<String, String>();
        _users.putIfAbsent("Omar", "password1");
        _users.putIfAbsent("Push", "password2");
        _users.putIfAbsent("Diffusion", "password3");
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onError(ErrorReason errorReason) {
        System.out.println(errorReason.toString());
    }

    @Override
    public void onRequest(JSON request, RequestContext context, Responder<JSON> responder) {
        try {
            final CBORFactory factory = new CBORFactory();
            final ObjectMapper mapper = new ObjectMapper();
            final TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
            };
            final String chatRole = "CHAT";

            final CBORParser parser = factory.createParser(request.asInputStream());
            final Map<String, String> map = mapper.readValue(parser, typeReference);

            String username = map.get("username");
            String password = map.get("password");
            // Additional improvements: Use password hashes instead of sending them in clear text.
            System.out.println(MessageFormat.format("Username: {0}; Password: {1};", username, password));

            if (_users.containsKey(username) && _users.get(username).equals(password)) {
                try {
                    ClientControl clientControl = _session.feature(ClientControl.class);
                    clientControl.changeRoles(context.getSessionId(), new HashSet<String>(),
                            new HashSet<String>(Arrays.asList(chatRole)));

                    responder.respond(Diffusion.dataTypes().json().fromJsonString("{ \"status\": \"OK\" }"));

                } catch (Exception e) {
                    responder.respond(Diffusion.dataTypes().json().fromJsonString("{ \"status\": \"Fail\", \"message\": \"Server Error.\" }"));
                }
            } else {
                responder.respond(Diffusion.dataTypes().json().fromJsonString("{ \"status\": \"Fail\", \"message\": \"Authentication Failed.\" }"));
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            responder.reject(e.toString());
        }
    }
}