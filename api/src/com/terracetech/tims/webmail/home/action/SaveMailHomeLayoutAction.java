package com.terracetech.tims.webmail.home.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.mailuser.User;

public class SaveMailHomeLayoutAction extends BaseAction {

    private static final long serialVersionUID = 20090109L;

    private MailHomeManager manager;

    public void setManager(MailHomeManager manager) {
        this.manager = manager;
    }

    public String execute() throws Exception {
        I18nResources resource = getMessageResource("setting");
        String setMailHome = request.getParameter("afterLogin");

        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));

        MailHomeLayoutVO layout = manager.readLayout();

        if (layout == null) {
            return "fail";
        }
        String msg = "";
        try {
            manager.deleteLayoutPortlet(layout, userSeq);
            manager.saveLayoutPortlet(layout, domainSeq, userSeq, setMailHome, request);
            msg = resource.getMessage("save.ok");
        }catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            msg = resource.getMessage("save.fail");
        }
        request.setAttribute("msg", msg);
        return "success";
    }
}
