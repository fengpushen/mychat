package com.xl.chat;

import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IStreamFilenameGenerator;

public class PathBean implements IStreamFilenameGenerator {

	public String recordPath = "streams/";
	public String playbackPath = "streams/";

	public String generateFilename(IScope scope, String name, GenerationType type) {
		return this.generateFilename(scope, name, null, type);
	}

	public String generateFilename(IScope scope, String name, String extension, GenerationType type) {
		String filename;
		if (type == GenerationType.RECORD)
			filename = recordPath + name;
		else
			filename = playbackPath + name;
		if (extension != null)
			filename += extension;
		return filename;
	}

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

	public String getPlaybackPath() {
		return playbackPath;
	}

	public void setPlaybackPath(String playbackPath) {
		this.playbackPath = playbackPath;
	}

	public boolean resolvesToAbsolutePath() {
		// TODO Auto-generated method stub
		return true;
	}

}
