public class ServerUtils {

	private static final String EOM; //The magic string that signifies End Of Message. generated using UUID.randomUUID(). Probably overkill for our needs.

	static{
		EOM = "2c727b25-2836-4ab0-b496-8100434ecb21";
	}
	public static String getEOM() {
		return EOM;
	}

}
