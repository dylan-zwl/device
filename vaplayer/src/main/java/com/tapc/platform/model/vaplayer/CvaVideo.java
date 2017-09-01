package com.tapc.platform.model.vaplayer;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class CvaVideo {
    public int totalFrames;
    public int levelID;
    public int framesPerSecondx1000;
    public int searchIndex = 0;
    public int nextFrame = 0;
    public int nextResistance = 0;
    public int nextIncline = 0;
    public String fileName;
    public VaLanguage vaLanguage;
    public VaVideoType vaVideoType;
    private String mpath;
    private Map<Integer, List<CvaEvent>> mLevelMap = new HashMap<Integer, List<CvaEvent>>();
    private Map<Integer, Map<Integer, String>> mLanguageMap = new HashMap<Integer, Map<Integer, String>>();

    public CvaVideo() {
        totalFrames = 0;
        levelID = 1;
        framesPerSecondx1000 = 29970;
        vaVideoType = VaVideoType.VT_WORKOUT;
    }

    public String getEventMessage(int Language, int _MessageID) {
        return mLanguageMap.get(Language).get(_MessageID);
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int mTotalFrames) {
        totalFrames = mTotalFrames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String pFileName) {
        fileName = pFileName;
    }

    public int getFramesPerSecondx1000() {
        return framesPerSecondx1000;
    }

    public void setFramesPerSecondx1000(int FramesPerSecondx1000) {
        this.framesPerSecondx1000 = FramesPerSecondx1000;
    }

    public VaVideoType getVideoType() {
        return vaVideoType;
    }

    public void setVideoType(VaVideoType Type) {
        this.vaVideoType = Type;
    }

    public VaLanguage getLanguage() {
        return vaLanguage;
    }

    public void setLanguage(VaLanguage Language) {
        vaLanguage = Language;
    }

    public boolean findNextEventFrame(int CurrentFrame, int SearchIndex) {
        int Size;
        int Frame;
        boolean Result = false;
        if (!checkLevelid(levelID))
            return Result;
        Size = mLevelMap.get(levelID).size();
        if ((SearchIndex < 0) || (SearchIndex >= Size))
            return Result;
        Frame = mLevelMap.get(levelID).get(SearchIndex).GetFrame();
        if (CurrentFrame >= Frame) {
            Result = true;
        }
        return Result;
    }

    public boolean findPreviousValueEventIndex(int CurrentFrame) {
        int Size;
        int Frame;
        boolean Result = false;
        if (!checkLevelid(levelID))
            return Result;
        Size = mLevelMap.get(levelID).size();
        for (int i = 0; i < Size; i++) {
            Frame = mLevelMap.get(levelID).get(i).GetFrame();
            searchIndex = i;
            if (Frame >= CurrentFrame) {
                Result = true;
                break;
            }
        }
        return Result;
    }

    public boolean findNextIncline(int SearchIndex) {
        int Size;

        if (!checkLevelid(levelID))
            return false;
        Size = mLevelMap.get(levelID).size();
        if ((SearchIndex < 0) || (SearchIndex >= Size))
            return false;

        for (int i = SearchIndex; i < Size; i++) {
            if (mLevelMap.get(levelID).get(i).GetIncline() != CvaEvent.INVALID_EVENT_VALUE) {
                nextIncline = mLevelMap.get(levelID).get(i).GetIncline();
                return true;
            }
        }
        return false;
    }

    public boolean findNextResistance(int SearchIndex) {
        int Size;

        if (!checkLevelid(levelID))
            return false;
        Size = mLevelMap.get(levelID).size();
        if ((SearchIndex < 0) || (SearchIndex >= Size))
            return false;

        for (int i = SearchIndex; i < Size; i++) {
            if (mLevelMap.get(levelID).get(i).GetResistance() != CvaEvent.INVALID_EVENT_VALUE) {
                nextResistance = mLevelMap.get(levelID).get(i).GetResistance();
                return true;
            }
        }
        return false;
    }

    public boolean checkLevelid(int Level) {
        if (!mLevelMap.containsKey(Level))
            return false;
        return true;
    }

    public boolean setLevel(int Level) {
        if (!mLevelMap.containsKey(Level))
            return false;
        levelID = Level;
        return true;
    }

    public int getLevel() {
        return levelID;
    }

    public int getLevelCount() {
        return mLevelMap.size();
    }

    public int getEventCount() {
        for (int i = 0; i < mLevelMap.get(levelID).size(); i++) {
            Log.d("ObjectEvent", " Frame:"
                    + mLevelMap.get(levelID).get(i).GetFrame() + " Resistance:"
                    + mLevelMap.get(levelID).get(i).GetResistance()
                    + " Incline:" + mLevelMap.get(levelID).get(i).GetIncline()
                    + " FileNameStringID:"
                    + mLevelMap.get(levelID).get(i).GetFileNameID()
                    + " MessageStringID:"
                    + mLevelMap.get(levelID).get(i).GetMessageID());
        }
        return mLevelMap.get(levelID).size();
    }

    public int getResistance() {
        return mLevelMap.get(levelID).get(searchIndex).GetResistance();
    }

    public int getIncline() {
        return mLevelMap.get(levelID).get(searchIndex).GetIncline();
    }

    public int getFileNameID() {
        return mLevelMap.get(levelID).get(searchIndex).GetFileNameID();
    }

    public int getMessageID() {
        return mLevelMap.get(levelID).get(searchIndex).GetMessageID();
    }

    public void reset() {
        totalFrames = 0;
        fileName = "";
        levelID = 1;
        mLevelMap.clear();
    }

    public void addEvent(CvaEvent Event, int LevelID) {
        if (mLevelMap.containsKey(LevelID)) {
            mLevelMap.get(LevelID).add(Event);
        } else {
            List<CvaEvent> listEvent = new ArrayList<CvaEvent>();
            listEvent.add(Event);
            mLevelMap.put(LevelID, listEvent);
        }
    }

    public String getEventString(int ID, VaLanguage Language) {
        if (!mLanguageMap.containsKey(Language))
            return "";
        return mLanguageMap.get(Language).get(ID);
    }

    public void addEventString(String pString, int ID, int LanguageID) {
        if (mLanguageMap.containsKey(LanguageID)) {
            mLanguageMap.get(LanguageID).put(ID, pString);
        } else {
            Map<Integer, String> map = new HashMap<Integer, String>();
            map.put(ID, pString);
            mLanguageMap.put(LanguageID, map);
        }
    }

    public String getPath() {
        return mpath;
    }

    public void setPath(String pDirectory) {
        mpath = pDirectory;
    }

    enum VaVideoType {
        VT_SPLASH, VT_WORKOUT, VT_COOLDOWN
    }

    enum VaLanguage {
        VAL_DEFAULT, VAL_ENGLIASH, VAL_ITALIAN, VAL_DUTCH, VAL_PORTUGUESE, VAL_CHINESE_SIMPLIFIED, VAL_SPANISH,
        VAL_CHINESE_TRADITIONAL, VAL_GERMAN8, VAL_JANPANESE, VAL_FRENCH
    }
}
