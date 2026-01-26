package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.manager.VCardManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.setting.vo.VCardVO;

public class ViewVcardAction extends BaseAction {

    private VCardManager vcardManager = null;
    private SettingManager settingManager = null;
    private VCardVO vcardVo = null;
    private String installLocale = null;

    public void setVcardManager(VCardManager vcardManager) {
        this.vcardManager = vcardManager;
    }

    public void setSettingManager(SettingManager settingManager) {
        this.settingManager = settingManager;
    }

    public String execute() throws Exception {

        User user = UserAuthManager.getUser(request);

        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));

        try {
            vcardVo = vcardManager.getVcardInfo(mailUserSeq);

            if (vcardVo == null) {
                UserInfoVO userInfoVo = settingManager.getUserInfo(mailUserSeq);
    
                if (userInfoVo != null) {
                    vcardVo = changeVcardVo(userInfoVo);
                    vcardVo.setUserSeq(mailUserSeq);
                    vcardManager.saveVcardInfo(vcardVo);
                }
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        installLocale = EnvConstants.getBasicSetting("setup.state");

        return "success";
    }

    public VCardVO getVcardVo() {
        return vcardVo;
    }

    public void setVcardVo(VCardVO vcardVo) {
        this.vcardVo = vcardVo;
    }

    public String getInstallLocale() {
        return installLocale;
    }

    private VCardVO changeVcardVo(UserInfoVO userInfoVo) {
        VCardVO vcardVo = new VCardVO();
        vcardVo.setMemberEmail(user.get(User.EMAIL));
        vcardVo.setMemberName(userInfoVo.getUserName());
        vcardVo.setFirstName(userInfoVo.getFirstName());
        vcardVo.setMiddleName(userInfoVo.getMiddleName());
        vcardVo.setLastName(userInfoVo.getLastName());
        vcardVo.setPrivateHomepage(userInfoVo.getPrivateHomepage());
        vcardVo.setMobileNo(userInfoVo.getMobileNo());
        vcardVo.setHomeTel(userInfoVo.getHomeTel());
        vcardVo.setHomeFax(userInfoVo.getHomeFax());
        vcardVo.setHomePostalCode(userInfoVo.getHomePostalCode());
        vcardVo.setHomeCountry(userInfoVo.getHomeCountry());
        vcardVo.setHomeState(userInfoVo.getHomeState());
        vcardVo.setHomeCity(userInfoVo.getHomeCity());
        vcardVo.setHomeStreet(userInfoVo.getHomeStreet());
        vcardVo.setHomeBasicAddress(userInfoVo.getHomeBasicAddress());
        vcardVo.setHomeExtAddress(userInfoVo.getHomeExtAddress());
        vcardVo.setCompanyName(userInfoVo.getCompanyName());
        vcardVo.setDepartmentName(userInfoVo.getDepartmentName());
        vcardVo.setOfficeTel(userInfoVo.getOfficeTel());
        vcardVo.setOfficeFax(userInfoVo.getOfficeFax());
        vcardVo.setOfficePostalCode(userInfoVo.getOfficePostalCode());
        vcardVo.setOfficeCountry(userInfoVo.getOfficeCountry());
        vcardVo.setOfficeState(userInfoVo.getOfficeState());
        vcardVo.setOfficeCity(userInfoVo.getOfficeCity());
        vcardVo.setOfficeStreet(userInfoVo.getOfficeStreet());
        vcardVo.setOfficeBasicAddress(userInfoVo.getOfficeBasicAddress());
        vcardVo.setOfficeExtAddress(userInfoVo.getOfficeExtAddress());

        return vcardVo;
    }

}
