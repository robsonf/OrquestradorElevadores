import java.util.ArrayList;
import java.util.Hashtable;


public class Testes {
	// número de baterias de testes a serem executados
	public static final int TOTAL_BATERIAS = 10;

	public static void main(String[] args) {
		Hashtable<String, ArrayList <Double>> relatorios = new Hashtable<String, ArrayList <Double>>();
		execucao(relatorios, "Dummy");
		execucao(relatorios, "Energia");
//		execucao(relatorios, "Tempo");
		execucao(relatorios, "Base");
		System.out.printf("\n\n\n########## RELATORIO FINAL ##############\n");
		System.out.println(String.format("Andares: %d Elevadores: %d Capacidade: %d Crescimento: %d%%", Orquestrador.NUM_ANDARES, Orquestrador.NUM_ELEVADORES, Elevador.CAPACIDADE_MAX, Orquestrador.PROBABILIDADE_CRESCIMENTO_POPULACAO));
		System.out.println("Dummy: " + relatorios.get("Dummy"));
		System.out.println("Energia: " + relatorios.get("Energia"));
//		System.out.println("Tempo: " + relatorios.get("Tempo"));
		System.out.println("Base: " + relatorios.get("Base"));
		
	}
	
	public static Hashtable<String, ArrayList <Double>> execucao(Hashtable<String, ArrayList <Double>> relatorios, String estrategia){
		ArrayList <Double> medias = new ArrayList <Double>();
		relatorios.put(estrategia, medias);
		
		ArrayList<Double> mediaTempos = new ArrayList<Double>();
		ArrayList<Double> mediaDistancias = new ArrayList<Double>();
		double acumulaTempo = 0.0;
		double acumulaDistancia = 0.0;
		
		ArrayList<Orquestrador> testes = new ArrayList<Orquestrador>(); 
		for (int i = 0; i < TOTAL_BATERIAS; i++) {
			switch (estrategia) {
			case "Dummy":
				testes.add(new Dummy());				
				break;
			case "Energia":
				testes.add(new ReducaoEnergia());				
				break;
			case "Tempo":
				testes.add(new ReducaoTempo());				
				break;
			case "Ambos":
				testes.add(new ReducaoAmbos());				
				break;
			case "Base":
				testes.add(new BaseLine());				
				break;
			}
		}
		for (Orquestrador o : testes) {
			mediaTempos.add(o.getMediaTempos());
			mediaDistancias.add(o.getMediaDistancias());
			acumulaTempo += o.getMediaTempos();
			acumulaDistancia += o.getMediaDistancias();
		}
		medias.add(acumulaTempo/TOTAL_BATERIAS);
		medias.add(acumulaDistancia/TOTAL_BATERIAS);

		return relatorios;
	}
}
