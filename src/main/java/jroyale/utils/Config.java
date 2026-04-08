package jroyale.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

	//---------------------------------------------------------------
	// STATIC CONSTANTS
	//---------------------------------------------------------------
	private final static boolean IS_DIST_VERSION = false; // this flag must be set to true when compiling for the dist version

	//---------------------------------------------------------------
	// STATIC ATTRIBUTE
	//---------------------------------------------------------------
	private static Config instance = null;

	//---------------------------------------------------------------
	// INSTANCE ATTRIBUTE
	//---------------------------------------------------------------
	private Properties properties;

	private Config() {
		try {
			String configFile = getConfigFile();
			BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "ISO-8859-1"));
			this.properties = new Properties();
			this.properties.load(buffRead);
		}
		catch(URISyntaxException urise) {
			urise.printStackTrace();
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	//---------------------------------------------------------------
	// PRIVATE INSTANCE METHODS
	//---------------------------------------------------------------
	private String getConfigFile() throws URISyntaxException {
		String configFile = null;
		String relPath = "\\conf\\config.txt";
		if (System.getProperty("os.name").startsWith("Linux")
		|| System.getProperty("os.name").startsWith("Mac")) {
			relPath = "/conf/config.txt";
		}
		if (IS_DIST_VERSION)
			configFile = getHomeFolderForDistVersion() + relPath;
		else
			configFile = getHomeFolderForDevVersion() + relPath;

		
		//return configFile;
		return "conf/config.txt";
	}

	private String getHomeFolderForDistVersion() throws URISyntaxException {
		String homeDir = null;
		String jarPath = Config.class.getResource("Config.class").toURI().toString();
		int indexOfExclamationMark = jarPath.indexOf("!");
		String prefix = "jar:file:/"; // this is the prefix for Windows OS platform
		if (System.getProperty("os.name").startsWith("Linux")) {
			prefix = "jar:file:";
		}
		homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);
		int lastIndexOfSlash = homeDir.lastIndexOf("/");
		homeDir = homeDir.substring(0, lastIndexOfSlash);
		return homeDir;
	}

	/*
	private String getHomeFolderForDistVersion() throws URISyntaxException {
		String homeDir = null;
		String jarPath = Config.class.getResource("Config.class").toURI().toString();
		if (System.getProperty("os.name").startsWith("Windows")) {
			int indexOfExclamationMark = jarPath.indexOf("!");
			String prefix = "jar:file:/";
			homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);
			int lastIndexOfSlash = homeDir.lastIndexOf("/");
			homeDir = homeDir.substring(0, lastIndexOfSlash);
			System.out.println("homeDir: " + homeDir);
		}
		else if (System.getProperty("os.name").startsWith("Linux")) {
			int indexOfExclamationMark = jarPath.indexOf("!");
			String prefix = "jar:file:";
			homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);
			int lastIndexOfSlash = homeDir.lastIndexOf("/");
			homeDir = homeDir.substring(0, lastIndexOfSlash);
		}
		return homeDir;
	}
	*/

	private String getHomeFolderForDevVersion() throws URISyntaxException {
		File configFile = null;
		File byteCodeFileOfThisClass = new File(Config.class.getResource("Config.class").toURI());
		//System.out.println("byteCodeFileOfThisClass: " + byteCodeFileOfThisClass);
		configFile = byteCodeFileOfThisClass.getParentFile().getParentFile().getParentFile().getParentFile();
		//System.out.println("configFile: " + configFile.toString());
		return configFile.toString();
	}

	//---------------------------------------------------------------
	// PUBLIC INSTANCE METHODS
	//---------------------------------------------------------------

    public String getDifficulty() {
        return this.properties.getProperty("difficulty");
    }

	public boolean isGridVisibleInBoard() {
		return this.properties.getProperty("isGridVisibleInBoard").toLowerCase().equals("true") ? true : false;
	}

	public boolean isGridVisibleInPreview() {
		return this.properties.getProperty("isGridVisibleInPreview").toLowerCase().equals("true") ? true : false;
	}

	public boolean isRefBlockRecognizableInBoard() {
		return this.properties.getProperty("isRefBlockRecognizableInBoard").toLowerCase().equals("true") ? true : false;
	}

	public boolean isRefBlockRecognizableInPreview() {
		return this.properties.getProperty("isRefBlockRecognizableInPreview").toLowerCase().equals("true") ? true : false;
	}

	public String getColorBackgroundBoard() {
		return this.properties.getProperty("colorBackgroundBoard");
	}

	public String getColorBackgroundPreview() {
		return this.properties.getProperty("colorBackgroundPreview");
	}

	public String getColorGridLineBoard() {
		return this.properties.getProperty("colorGridLineBoard");
	}

	public String getColorGridLinePreview() {
		return this.properties.getProperty("colorGridLinePreview");
	}

	public String getColorOfPieceI() {
		return this.properties.getProperty("colorPieceI");
	}

	public String getColorOfPieceJ() {
		return this.properties.getProperty("colorPieceJ");
	}

	public String getColorOfPieceL() {
		return this.properties.getProperty("colorPieceL");
	}

	public String getColorOfPieceO() {
		return this.properties.getProperty("colorPieceO");
	}

	public String getColorOfPieceS() {
		return this.properties.getProperty("colorPieceS");
	}

	public String getColorOfPieceT() {
		return this.properties.getProperty("colorPieceT");
	}

	public String getColorOfPieceZ() {
		return this.properties.getProperty("colorPieceZ");
	}

	public String getColorForOutlineOfPieceI() {
		return this.properties.getProperty("colorOutlinePieceI");
	}

	public String getColorForOutlineOfPieceJ() {
		return this.properties.getProperty("colorOutlinePieceJ");
	}

	public String getColorForOutlineOfPieceL() {
		return this.properties.getProperty("colorOutlinePieceL");
	}

	public String getColorForOutlineOfPieceO() {
		return this.properties.getProperty("colorOutlinePieceO");
	}

	public String getColorForOutlineOfPieceS() {
		return this.properties.getProperty("colorOutlinePieceS");
	}

	public String getColorForOutlineOfPieceT() {
		return this.properties.getProperty("colorOutlinePieceT");
	}

	public String getColorForOutlineOfPieceZ() {
		return this.properties.getProperty("colorOutlinePieceZ");
	}

	public int getDelayTime() {
		return Integer.valueOf(this.properties.getProperty("delayTime"));
	}

	//---------------------------------------------------------------
	// STATIC METHODS
	//---------------------------------------------------------------
	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

} 