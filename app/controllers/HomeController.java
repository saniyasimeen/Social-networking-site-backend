package controllers;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.User;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by lubuntu on 8/21/16.
 */
public class HomeController extends Controller {
    @Inject
    ObjectMapper objectMapper;
    @Inject
    FormFactory formFactory;
    public Result getProfile(Long userId){
        User user= User.find.byId(userId);
        Profile profile=Profile.find.byId(user.profile.id);
        ObjectNode data= objectMapper.createObjectNode();
        List<Long> connectedUserIds= user.connections.stream().map(x->x.id).collect(Collectors.toList());
        List<Long> connectionRequestSentUserIds= user.connectionRequestsSent.stream().
                map(x->x.receiver.id).collect(Collectors.toList());
        List<JsonNode> suggestions= User.find.all().stream()
                .filter(x->!connectedUserIds.contains(x.id) &&
                                    !connectionRequestSentUserIds.contains(x.id) &&
                                    !Objects.equals(x.id,userId))
                .map(x->{
                    ObjectNode userJson= objectMapper.createObjectNode();
                    Profile profile1 = Profile.find.byId(x.profile.id);
                    userJson.put("email",x.email);
                    userJson.put("id",x.id);
                    userJson.put("firstName",profile1.firstName);
                    userJson.put("lastName",profile1.lastName);
                    return userJson;
                }).collect(Collectors.toList());
        data.set("suggestion",objectMapper.valueToTree(suggestions));
        data.set("connections",objectMapper.valueToTree(user.connections.stream()
                .map(x->{
            User connectedUser= User.find.byId(x.id);
            Profile connectedProfile=Profile.find.byId(connectedUser.profile.id);
            ObjectNode connectionJson= objectMapper.createObjectNode();
            connectionJson.put("email",connectedUser.email);
            connectionJson.put("firstName",connectedProfile.firstName);
            connectionJson.put("lastName",connectedProfile.lastName);
            return connectionJson;
        }).collect(Collectors.toList())));
        return ok();
    }
}
