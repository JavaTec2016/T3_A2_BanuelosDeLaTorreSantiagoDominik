import java.util.ArrayList;

import javax.swing.*;
class Reloj extends Thread {
	Ventana jtruco;
	String suspensin = "";
	double tasa = 60;
	int tiempo = 0;
	
	public Reloj(Ventana t) {
		jtruco = t;
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			//se muere si el recolector de texto ya acabo
			
			
			sleep((int)(1000/tasa));
			if(jtruco.listo || !jtruco.hiloTextos.isAlive())return;
			//double ratio = tiempo / tasa;
			if(tiempo >= tasa/20) {
				suspensin+="O";
				if(suspensin.length() > 3) suspensin = "O";
				jtruco.areaSi.setText(suspensin);
				jtruco.areaNo.setText(suspensin);
				tiempo = 0;
			}
			jtruco.siConteo.setText("SI: "+jtruco.numSi);
			jtruco.noConteo.setText("NO: "+jtruco.numNo);
			
			tiempo++;
			//if(jtruco.listo || !jtruco.hiloTextos.isAlive())return;
			run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
class textoChequeoLento implements Runnable {
	Ventana v;
	public textoChequeoLento(Ventana f) {
		v = f;
	}
	@Override
	public void run() {
		
		for(String d : v.a.datos) {
			if(d.equalsIgnoreCase("si")) {
				v.numSi++;
				v.sSi = (d+" \n")+v.sNo;
			}else {
				v.numNo++;
				v.sNo = (d+" \n")+v.sNo;
							
			}
		}
		//v.estado.setText("Estructurando...");
		
		v.areaSi.setText(v.sSi);
		v.areaNo.setText(v.sNo);
		System.out.println("acabao");
		v.listo = true;
	}
}
class textoChequeo implements Runnable {
	Ventana v;
	public textoChequeo(Ventana f) {
		v = f;
	}
	@Override
	public void run() {
		// optimizacion maestra \0/
		ArrayList<String> sis = new ArrayList<String>();
		ArrayList<String> non = new ArrayList<String>();
		
		for(String d : v.a.datos) {
			if(d.equalsIgnoreCase("si")) {
				v.numSi++;
				sis.add(d+" \n");
			}else {
				v.numNo++;
				non.add(d+" \n");			
			}
		}
		v.estado.setText("Estructurando...");
		v.sSi = sis.toString();
		v.sNo = non.toString();
		v.sSi = v.sSi.substring(1, v.sSi.length()-1);
		v.sNo = v.sNo.substring(1, v.sNo.length()-1);
		System.out.println("acabao");
		v.listo = true;
		v.areaSi.setText(v.sSi);
		v.areaNo.setText(v.sNo);
		
		
		
	}
	
}
class BarraChequeo extends Thread {
	Ventana v;
	public BarraChequeo(Ventana v) {
		this.v = v;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		v.hiloTextos.start();
		
		try {
			
			
			v.estado.setText("Generando histograma...");
			
			String[] largoSi = v.sSi.split(", ");
			String[] largoNo = v.sNo.split(", ");
			
			while(v.hiloTextos.isAlive()) {
				
				v.barraSi.setValue((int)((double)v.numSi/v.a.datos.size()*100));
				v.barraNo.setValue((int)((double)v.numNo/v.a.datos.size()*100));
			}
			v.estado.setText("Resultados listos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class Ventana extends JFrame {
	int width = 26, height = 26;
	JProgressBar barraSi = new JProgressBar(SwingConstants.VERTICAL, 0, 100);
	JProgressBar barraNo = new JProgressBar(SwingConstants.VERTICAL, 0, 100);
	
	JLabel siConteo = new JLabel("SI");
	JLabel noConteo = new JLabel("NO");
	
	JLabel estado = new JLabel("- - -");
	
	String sSi = "";
	JTextArea areaSi = new JTextArea(5,5);
	
	String sNo = "";
	JTextArea areaNo = new JTextArea(5,5);
	
	JScrollPane wrapSi = new JScrollPane(areaSi);
	JScrollPane wrapNo = new JScrollPane(areaNo);
	Analizador a = new Analizador();
	
	Thread hiloTextos = new Thread(new textoChequeo(this));
	BarraChequeo hiloBarra = new BarraChequeo(this);
	
	int numSi = 0;
	int numNo = 0;
	boolean listo = false;
	//constructor
	public Ventana() {
		
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Encuesta");
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setVisible(true);
		dimensionar(new JLabel("RESULTADOS DE LA ENCUESTA: "), 3, 3, 10, 1);
		dimensionar(estado, 13, 3, 8, 1);
		dimensionar(siConteo, 3, 5, 10, 1);
		dimensionar(wrapSi, 3, 7, 10, 15);
		dimensionar(noConteo, 14, 5, 10, 1);
		dimensionar(wrapNo, 14, 7, 10, 15);
		
		dimensionar(new JLabel("SI"), 28, 18, 2, 1);
		dimensionar(new JLabel("NO"), 32, 18, 2, 1);
		dimensionar(barraSi, 27, 7, 3, 10);
		dimensionar(barraNo, 31, 7, 3, 10);
		
	}
	void dimensionar(JComponent c, int x, int y, double w, double h) {
		c.setBounds(x*width,y*height,(int)w*width,(int)h*height);
		add(c);
	}
	void dimensionar(JComponent c, int x, int y) {
		c.setBounds(x*width,y*height, c.getWidth(), c.getHeight());
		add(c);
	}
	void capturarDatos() {
		listo = false;
		numSi = 0; numNo = 0;
		estado.setText("procesando...");
		a.generarDatos();
		
		
	}
}
public class Gui {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ventana vn = new Ventana();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				vn.capturarDatos();
				Reloj r = new Reloj(vn);
				
				
				vn.hiloBarra.start();
				r.start();
				
			}
		});
	}

}
