package controllers;

import com.avaje.ebean.Expr;
import models.ConnectionRequest;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by lubuntu on 8/21/16.
 */
public class Connections extends Controller {
    public Result sendConnectionRequest(Long senderId,Long toId){
        if(ConnectionRequest.find.where().and(Expr.eq("sender_id",senderId),Expr.eq("receiver_id",toId))
                .findUnique()==null){
            ConnectionRequest connectionRequest=new ConnectionRequest();
            connectionRequest.sender = User.find.byId(senderId);
            connectionRequest.receiver = User.find.byId(toId);
            connectionRequest.status = ConnectionRequest.Status.WAITING;

            ConnectionRequest.db().save(connectionRequest);

            User sender = User.find.byId(connectionRequest.sender.id);
            sender.connectionRequestsSent.add(connectionRequest);
            User.db().update(sender);

            User receiver = User.find.byId(connectionRequest.receiver.id);
            receiver.connectionRequestsReceived.add(connectionRequest);
            User.db().update(receiver);
        }
        return ok();
    }
}
