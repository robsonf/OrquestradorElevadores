import java.util.ArrayList;

public class Orquestrador {

	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido;
	private int tempoTotal;
	
	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>(2);
		andares.add(new Andar(0));
		andares.add(new Andar(1));

		elevadores = new ArrayList<Elevador>(2);
		elevadores.add(new Elevador(0));
		
	}
	
	public static void main(String[] args) {
		Orquestrador o = new Orquestrador();
		System.out.println(o);
	}
	
	@Override
	public String toString() {
		String aux = String.format("TempoDecorrido: %d TempoTotal: %d\nAndares:\n", tempoDecorrido, tempoTotal);
		for (Andar a : andares) {
			aux += a;
		}
		aux += "Elevadores:\n";
		for (Elevador e : elevadores) {
			aux += e + "\n";
		}
		return aux;
	}


}
