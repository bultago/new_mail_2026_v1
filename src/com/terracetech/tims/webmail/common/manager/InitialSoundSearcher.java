package com.terracetech.tims.webmail.common.manager;

import org.apache.log4j.Logger;

import com.terracetech.tims.webmail.util.StringUtils;

public class InitialSoundSearcher {
    private Logger log = Logger.getLogger(this.getClass());
    private final char HANGUL_START_UNICODE = '가'; //OxAC00
    private final char HANGUL_END_UNICODE = '힣'; //OxD7A3
    private final char[] INITIAL_SOUND = {'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'};
    private final char HANGUL_ONE_INITIAL_SOUND_COUNT = 21 * 28;//중성 * 종성
    
    public boolean isHangulChar(char hangulChar) {
        return HANGUL_START_UNICODE <= hangulChar && hangulChar <= HANGUL_END_UNICODE;
    }

    public boolean isInitialSoundChar(char inputChar) {
        for (char initalSoundChar : INITIAL_SOUND) {
            if (initalSoundChar == inputChar) {
                return true;
            }
        }
        return false;
    }

    public char parseInitialSound(char orgChar) {
        int hangulBegin = orgChar - HANGUL_START_UNICODE;
        int index = (hangulBegin / HANGUL_ONE_INITIAL_SOUND_COUNT);
        return INITIAL_SOUND[index];
    }

    public int targetLengthMinusSearchTermLength(String target, String searchTerm) {
        return target.length() - searchTerm.length();
    }

    public boolean isHangleString(String hangulStr) {
        char[] hangulCharArray = makeStringToCharArray(hangulStr);
        for (int i=0; i<hangulCharArray.length; i++) {
            if (!isHangulChar(hangulCharArray[i])) {
                return false;
            }
        }
        return true;
    }
    
    public char[] makeStringToCharArray(String str) {
        return str.toCharArray();
    }
    
    public boolean checkInitialSoundChar(char searchChar, char targetChar) {
        if (isInitialSoundChar(searchChar) && isHangulChar(targetChar)) {
            if (searchChar != parseInitialSound(targetChar)) {
                return false;
            }
            return true;
        } else {
            if (searchChar != targetChar) {
                return false;
            }
            return true;
        }
    }

    public boolean initialSoundSearchMatch(String searchTarget, String searchTerm) {
        
        if (StringUtils.isEmpty(searchTarget) || StringUtils.isEmpty(searchTerm)) return false;
        
        if (targetLengthMinusSearchTermLength(searchTarget, searchTerm) < 0) return false;
        
        char[] searchTermCharArray = makeStringToCharArray(searchTerm);
        char[] searchTargetCharArray = makeStringToCharArray(searchTarget);
        for (int i=0; i<=targetLengthMinusSearchTermLength(searchTarget, searchTerm); i++) {
            int searchPoint = 0;
            while (searchPoint < searchTermCharArray.length) {
                if (!checkInitialSoundChar(searchTermCharArray[searchPoint], searchTargetCharArray[searchPoint+i])) {
                    break;
                }
                searchPoint++;
            }
            if (searchPoint == searchTermCharArray.length) {
                return true;
            }
        }
        return false;
    }
}
