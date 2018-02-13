package fss.shopping.web.service;

import java.io.IOException;

import fss.shopping.model.Role;
import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 09.02.2018.
 */

public class UserRolesRequest extends AbstractRequest {

    public UserRolesRequest(RequestBody requestBody) {
        super(requestBody);
    }

    @Override
    protected String getRequestUrl() {
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }

    public interface Listener {
        public void onUserRolesSuccess(Role role);

        public void onUserRolesFailure(String errorMessage);
    }
}
