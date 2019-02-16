package main.java.com.slimtrade.core.audio;

public enum Sound {
	
	PING1("Ping 1", "/resources/audio/ping1.wav"),
	PING2("Ping 2", "/resources/audio/pingg.wav"),
	CLICK1("Click 1", "/resources/audio/click3.wav"),
	;
	
	private String name;
	private String path;
	
	Sound(String name, String path){
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name.toUpperCase().replaceAll("\\s+", "");
	}

	public String getPath() {
		return path;
	}
	@Override
	public String toString(){
		return name;
	}
	
}
