package com.github.miho73.legacy.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {
    public enum PRIVILEGES {
        USER,
        ROOT
    }

    public int privilegeOf(boolean user, boolean root) {
        int priv = 0;
        if(user) priv += 1;
        if(root) priv += 2;
        return priv;
    }

    /**
     * check if session has given privilege.
     * @param session session to check
     * @return true when session has eligible privilege
     */
    public boolean checkPrivilege(HttpSession session, int privilege) {
        if(session == null) return false;
        Integer sp = (Integer)session.getAttribute("priv");
        if(sp == null) return false;
        boolean flag = true;

        while(true) {
            boolean i = sp % 2 == 1;
            boolean u = privilege % 2 == 1;
            flag = flag && (i || !u);
            sp /= 2;
            privilege /= 2;
            if(sp == 0 && privilege == 0) break;
        }
        return flag;
    }
}
