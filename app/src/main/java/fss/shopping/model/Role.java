package fss.shopping.model;

import java.util.List;

/**
 * Created by Alex on 09.02.2018.
 */

public class Role {
    private String name;
    private List<String> privileges;

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, List<String> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public void addPrivilege(String privilege) {
        privileges.add(name);
    }
}
