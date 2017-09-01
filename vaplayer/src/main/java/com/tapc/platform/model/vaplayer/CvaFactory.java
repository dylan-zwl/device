package com.tapc.platform.model.vaplayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class CvaFactory {
    private final int OBJECT_ID_VIDEO = 1;
    private final int OBJECT_ID_STRING = 2;
    private final int OBJECT_ID_LEVEL = 3;
    private final int OBJECT_ID_EVENT = 4;

    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF) | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000) | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    public Object GetObject(InputStream inputStream, int Index, int size) {
        Object object = null;
        try {
            byte[] Buffer = new byte[size];
            inputStream.read(Buffer, Index, size);
            // for (byte e : Buffer) {
            // Log.d("Buffer###1", Integer.toHexString(e & 0xFF));
            // }
            ByteArrayInputStream bis = new ByteArrayInputStream(Buffer);
            ObjectInputStream ois = new ObjectInputStream(bis);
            object = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public boolean DeserializeVideo(InputStream inputStream, int BufferSize,
                                    CvaVideo Video) {
        ObjectSize objectSize = new ObjectSize();
        ObjectHeader pObject = new ObjectHeader();
        ObjectString pObjectString = new ObjectString();
        ObjectVideo pObjectVideo = new ObjectVideo();
        ObjectLevel pObjectLevel = new ObjectLevel();
        ObjectEvent pObjectEvent = new ObjectEvent();
        int Index = 0;
        while (Index <= BufferSize) {
            if (pObject.Size <= BufferSize - Index) {
                pObject.setDate(inputStream, objectSize.HeaderSize);
                switch (pObject.ID) {
                    case OBJECT_ID_VIDEO:
                        pObjectVideo.setDate(inputStream, objectSize.VideoSize);
                        Video.reset();
                        Video.setTotalFrames(pObjectVideo.TotalFrames);
                        Video.setFileName(pObjectVideo.VideoFile);
                        Video.setFramesPerSecondx1000(pObjectVideo.FramesPerSecondx1000);
                        break;
                    case OBJECT_ID_STRING:
                        pObjectString.setDate(inputStream, pObject.Size
                                - objectSize.HeaderSize, objectSize.StringSize);
                        Video.addEventString(pObjectString.MsgString,
                                pObjectString.StringID,
                                pObjectString.LanguageID);
                        break;
                    case OBJECT_ID_LEVEL:
                        pObjectLevel.setDate(inputStream, objectSize.LevelSize);
                        break;
                    case OBJECT_ID_EVENT:
                        pObjectEvent.setDate(inputStream, objectSize.EventSize);
                        CvaEvent mEvent = new CvaEvent();
                        mEvent.SetFrame(pObjectEvent.Frame);
                        mEvent.SetResistance(pObjectEvent.Resistance);
                        mEvent.SetIncline(pObjectEvent.Incline);
                        mEvent.SetFileNameID(pObjectEvent.FileNameStringID);
                        mEvent.SetMessageID(pObjectEvent.MessageStringID);
                        Video.addEvent(mEvent, pObjectEvent.LevelID);
                        break;
                }
                Index += pObject.Size;
                if (pObject.Size == 0) {
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }

    class ObjectSize {
        int HeaderSize = 12;
        int StringSize = 12;
        int VideoSize = 112;
        int LevelSize = 4;
        int EventSize = 32;
    }

    class ObjectHeader {
        int ID;
        int Size;
        int Version;

        public void setDate(InputStream inputStream, int size) {
            try {
                byte[] Buffer = new byte[size];
                inputStream.read(Buffer, 0, size);
                ID = bytesToInt(Buffer, 0);
                Size = bytesToInt(Buffer, 4);
                Version = bytesToInt(Buffer, 8);
                // Log.d("ObjectHeader", " " + ID + " " + Size + " " + Version);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ObjectString {
        int StringID;
        int LanguageID;
        int Type;
        String MsgString;

        public void setDate(InputStream inputStream, int size, int objectSize) {
            try {
                byte[] Buffer = new byte[size];
                inputStream.read(Buffer, 0, size);
                StringID = bytesToInt(Buffer, 0);
                LanguageID = bytesToInt(Buffer, 4);
                Type = bytesToInt(Buffer, 8);
                MsgString = new String(Buffer, objectSize, size - objectSize)
                        .trim();
                // Log.d("ObjectString", "StringID:" + StringID + " LanguageID:"
                // + LanguageID + " Type:" + Type + " MsgString:"
                // + MsgString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ObjectVideo {
        int TotalFrames;
        String VideoFile;
        int Reserved;
        int FramesPerSecondx1000;

        public void setDate(InputStream inputStream, int size) {
            try {
                byte[] Buffer = new byte[size];
                inputStream.read(Buffer, 0, size);
                TotalFrames = bytesToInt(Buffer, 0);
                VideoFile = new String(Buffer, 4, 104).trim();
                Reserved = bytesToInt(Buffer, 104);
                FramesPerSecondx1000 = bytesToInt(Buffer, 108);
                // Log.d("ObjectVideo", "TotalFrames:" + TotalFrames
                // + " VideoFile:" + VideoFile + " Reserved:" + Reserved
                // + " FramesPerSecondx1000:" + FramesPerSecondx1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ObjectLevel {
        int LevelID;

        public void setDate(InputStream inputStream, int size) {
            try {
                byte[] Buffer = new byte[size];
                inputStream.read(Buffer, 0, size);
                LevelID = bytesToInt(Buffer, 0);
                // Log.d("ObjectLevel", "LevelID:" + LevelID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ObjectEvent {
        int LevelID;
        int Frame;
        int Resistance;
        int Incline;
        int FileNameStringID;
        int MessageStringID;
        int Flags;
        int Parameter;

        public void setDate(InputStream inputStream, int size) {
            try {
                byte[] Buffer = new byte[size];
                inputStream.read(Buffer, 0, size);
                LevelID = bytesToInt(Buffer, 0);
                Frame = bytesToInt(Buffer, 4);
                Resistance = bytesToInt(Buffer, 8);
                Incline = bytesToInt(Buffer, 12);
                FileNameStringID = bytesToInt(Buffer, 16);
                MessageStringID = bytesToInt(Buffer, 20);
                Flags = bytesToInt(Buffer, 24);
                Parameter = bytesToInt(Buffer, 28);
                // Log.d("ObjectEvent", "LevelID:" + LevelID + " Frame:" + Frame
                // + " Resistance:" + Resistance + " Incline:" + Incline
                // + " FileNameStringID:" + FileNameStringID
                // + " MessageStringID:" + MessageStringID + " Flags:"
                // + Flags + " Parameter:" + Parameter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
