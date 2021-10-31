package tools;

public class Log {
    public static int counter=0;
    public static void log(String m){
        System.out.println(new Exception().getStackTrace()[1].toString()+" : "+m);
    }
    public static void error(String m){
        System.err.println(new Exception().getStackTrace()[1].toString()+" : "+m);
    }
    public static void count(){
        counter++;
        System.out.println(new Exception().getStackTrace()[1].toString()+" : "+counter);
    }
    public static void reset(){
        counter=0;

    }
}
