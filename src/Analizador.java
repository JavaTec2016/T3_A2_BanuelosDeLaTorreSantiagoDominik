import java.util.ArrayList;
import java.util.Random;

public class Analizador {
	ArrayList<String> datos = new ArrayList<String>();
	int max = 10000000;
	boolean generarDatos() {
		Random r = new Random();
		int sem = r.nextInt(20);
		for(int i = 0; i < max; i ++) {
			if(r.nextInt(20) >= sem) datos.add("SI");
			else datos.add("NO");
		}
		return true;
	}
}
