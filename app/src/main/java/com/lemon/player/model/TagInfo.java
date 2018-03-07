package com.lemon.player.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * A holder for various id3 tag information associated with a file.
 */
public class TagInfo implements Serializable {

    public String artistName;
    public String albumArtistName;
    public String albumName;
    public String trackName;
    public int trackNumber;
    public int trackTotal;
    public int discNumber;
    public int discTotal;
    public String bitrate;
    public String format;
    public int sampleRate;
    public String genre;

    public TagInfo(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    AudioFile audioFile = AudioFileIO.read(file);
                    this.artistName = getTag(audioFile, FieldKey.ARTIST);
                    this.albumArtistName = getTag(audioFile, FieldKey.ALBUM_ARTIST);
                    this.albumName = getTag(audioFile, FieldKey.ALBUM);
                    this.trackName = getTag(audioFile, FieldKey.TITLE);
                    this.trackNumber = parseInt(getTag(audioFile, FieldKey.TRACK));
                    this.trackTotal = parseInt(getTag(audioFile, FieldKey.TRACK_TOTAL));
                    this.discNumber = parseInt(getTag(audioFile, FieldKey.DISC_NO));
                    this.discTotal = parseInt(getTag(audioFile, FieldKey.DISC_TOTAL));
                    this.bitrate = getBitrate(audioFile);
                    this.format = getFormat(audioFile);
                    this.sampleRate = getSampleRate(audioFile);
                    this.genre = getTag(audioFile, FieldKey.GENRE);
                } catch (CannotReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TagException e) {
                    e.printStackTrace();
                } catch (ReadOnlyFileException e) {
                    e.printStackTrace();
                } catch (InvalidAudioFrameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTag(AudioFile audioFile, FieldKey key) {
        try {
            if (audioFile != null) {
                Tag tag = audioFile.getTag();
                if (tag != null) {
                    String result = tag.getFirst(key);
                    if (!TextUtils.isEmpty(result)) {
                        return result;
                    }
                }
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return "Unknown";
    }

    public static String getBitrate(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getBitRate();
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return "Unknown";
    }

    public static String getFormat(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getFormat();
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return "Unknown";
    }

    public static int getSampleRate(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getSampleRateAsNumber();
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return -1;
    }

    int parseInt(@Nullable String string) {
        if (string != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignored) {

            }
        }
        return -1;
    }
}