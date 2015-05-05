import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ReducaoTempo extends Orquestrador {

	private ArrayList<Integer> [] listaAtendimentos;
	private Integer [] listaPossiveisAtendimentos;
	
	public ReducaoTempo() {
		listaAtendimentos = new ArrayList [Orquestrador.NUM_ELEVADORES];
		listaPossiveisAtendimentos = new Integer [(Orquestrador.NUM_ANDARES*2)-2];
		programarAlocacaoAtendimentos();
		super.inicializar();
	}

	/*
	 * define uma associação entre a lista de andares chamados, 
	 * e os possiveis andares atendidos por cada elevador.
	 * A estrutura da lista de possiveis andares atendidos segue como o exemplo:
	 * listaPossiveisAtendimentos [0,1,-1,2,-2,3,-3,...,-n],
	 * onde n é o numero de andares, os positivos são chamadas de subida, 
	 * os negativos de descida no respectivo andar.
	 * Para elevador [0], listaAtendimentos = [listaPossiveisAtendimentos[0], listaPossiveisAtendimentos[10],...] 
		0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6, 7, -7, 8, -8, 9, -9, 10, -10, 11, -11, 12, -12, 13, -13, 14, -14, 15, -15, 16, -16, 17, -17, 18, -18, -19, 
		10, 6, 18, 12, -5, 11, 4, 1, -10, -13, -1, 3, 16, 15, -9, 13, 2, -19, 5, -7, 17, 9, -14, -6, -8, 0, 14, -2, 7, -3, -17, -12, -18, -16, -15, -4, -11, 8, 
		[10, -1, 17, -17]
		[6, 3, 9, -12]
		[18, 16, -14, -18]
		[12, 15, -6, -16]
		[-5, -9, -8, -15]
		[11, 13, 0, -4]
		[4, 2, 14, -11]
		[1, -19, -2, 8]
		[-10, 5, 7]
		[-13, -7, -3]
	 */
	private void programarAlocacaoAtendimentos() {
		construirListaAtendimentos();
		associarAtendimentos();
	}

	private void construirListaAtendimentos() {
		listaPossiveisAtendimentos[0] = 0;
		listaPossiveisAtendimentos[listaPossiveisAtendimentos.length-1] = -(Orquestrador.NUM_ANDARES-1);
		for (int i = 1; i < Orquestrador.NUM_ANDARES-1; i++) {
			listaPossiveisAtendimentos[(i*2)-1] = i;
			listaPossiveisAtendimentos[i*2] = -i;
		}
		List<?> list = Arrays.asList(listaPossiveisAtendimentos);
		Collections.shuffle(list);
		list.toArray(listaPossiveisAtendimentos);
	}
	/*
	 * distribui as associações entre chamadas e atendimentos, igualmente por elevador
	 */
	private void associarAtendimentos() {
		for (int i = 0; i < listaAtendimentos.length; i++) {
			listaAtendimentos[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < listaPossiveisAtendimentos.length; i++) {
			listaAtendimentos[i%Orquestrador.NUM_ELEVADORES].add(listaPossiveisAtendimentos[i]);
		}
	}
	
	public void tomarDecisoes(){
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
			LinkedHashSet<Integer> todasChamadas = new LinkedHashSet<Integer>();
			todasChamadas.addAll(listaChamadasSubida);
			todasChamadas.addAll(listaChamadasDescida);
			ArrayList<Integer> podeAtender  = listaAtendimentos[elevador.getId()];
			LinkedHashSet<Integer> atendidas = filtrarChamadas(todasChamadas, podeAtender); 
			elevador.setAtendeChamadas(atendidas);
			if(atendidas.isEmpty() && elevador.getPessoas().isEmpty()) {
				elevador.setAcao(Elevador.PARAR);
				elevador.setStatus(Elevador.PARAR);
				continue;
			}
			// alterar o teto e chao de acordo com as chamadas e destinos do usuario
			atualizarLimites(elevador, atendidas);
			// se parado, direciona o elevador para a mesma direcao do maior número de chamadas
			if(elevador.getStatus() == Elevador.PARAR){
				if(!atendidas.isEmpty()){
					elevador.setStatus(Elevador.SUBIR);
//					continue;
				}
			}
			if(andarAtual == elevador.getChao() && andarAtual == elevador.getTeto()){
				elevador.setAcao(Elevador.PARAR);
			}else if (elevador.getStatus() == Elevador.SUBIR) {
				if(andarAtual == elevador.getTeto())
					elevador.setAcao(Elevador.PARAR);
				if (andarAtual < elevador.getTeto()) {
					elevador.setAcao(Elevador.SUBIR);
				} else {
					elevador.setAcao(Elevador.DESCER);
					elevador.setStatus(Elevador.DESCER);
				}
			} else {
				if (andarAtual > elevador.getChao()) {
					elevador.setAcao(Elevador.DESCER);
				} else {
					elevador.setAcao(Elevador.SUBIR);
					elevador.setStatus(Elevador.SUBIR);
				}
			}
//			if (elevador.getStatus() == Elevador.SUBIR) {
//				if(andarAtual == elevador.getTeto()){
//					elevador.setAcao(Elevador.PARAR);
//				}else if (andarAtual < elevador.getTeto()) {
//					elevador.setAcao(Elevador.SUBIR);
//				} else {
//					elevador.setAcao(Elevador.DESCER);
//					elevador.setStatus(Elevador.DESCER);
//				}
//			} else {
//				if(andarAtual == elevador.getChao()){
//					elevador.setAcao(Elevador.PARAR);
//				}else if (andarAtual > elevador.getChao()) {
//					elevador.setAcao(Elevador.DESCER);
//				} else {
//					elevador.setAcao(Elevador.SUBIR);
//					elevador.setStatus(Elevador.SUBIR);
//				}
//			}
			// elevador para caso alguem sobe ou desce
			if (elevador.alguemSai() || (elevador.alguemEntra() && !elevador.estaLotado())){
				elevador.setAcao(Elevador.PARAR);
				continue;
			}
		}
	}
	private void atualizarLimites(Elevador elevador, LinkedHashSet<Integer> atendidas) {
		ArrayList<Integer> limites = new ArrayList<Integer>();
		adicionaLimites(atendidas.toArray(), limites);
		adicionaLimites(elevador.getListaDestinos().toArray(), limites);
		if(limites.isEmpty())
			limites.add(0);
		Collections.sort(limites);
		elevador.setChao(limites.get(0));
		elevador.setTeto(limites.get(limites.size()-1));
	}
	public void adicionaLimites(Object [] lista, ArrayList<Integer> limites){
		if (lista.length > 0) {
			Arrays.sort(lista);
			limites.add(Math.abs((int) lista[0]));
			limites.add(Math.abs((int) lista[lista.length - 1]));
		}
	}
	public void notificarAtualizacaoChamadas(){
	}
	private LinkedHashSet<Integer> filtrarChamadas(LinkedHashSet<Integer> chamadas, ArrayList<Integer> atendidas){
		LinkedHashSet<Integer> filtro = new LinkedHashSet<Integer>();
		for (int chamada : chamadas) {
			if(atendidas.contains(chamada) || atendidas.contains(-chamada)){
				filtro.add(chamada);
			}
		}
		return filtro;
	}
	@Override
	public String toString() {
		String aux = super.toString();
		aux += "Lista Atendimentos:\n";
		for (int i = 0; i < listaAtendimentos.length; i++) {
			aux += String.format("\t%d: %s\n", i , listaAtendimentos[i].toString());
		}
		return aux;
	}
}