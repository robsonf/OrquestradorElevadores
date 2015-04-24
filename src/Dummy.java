import java.util.LinkedList;


public class Dummy extends Orquestrador {

	public Dummy() {
		super();
	}

	public void tomarDecisoes(){
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
			Andar andar = andares.get(andarAtual);
			LinkedList<Pessoa> pessoasAndar = andar.getPessoas();
			LinkedList<Pessoa> pessoasElevador = elevador.getPessoas();
			
			// elevador pára esperando alguem sair
			if (this.alguemSair(andarAtual, pessoasElevador)){
				  elevador.setAcao(Elevador.PARAR);
				  continue;
			}
			// elevador pessoas esperando no andar e elevador em movimento entao parar
			if (!pessoasAndar.isEmpty() && elevador.getAcao() != Elevador.PARAR){
				  elevador.setAcao(Elevador.PARAR);
				  continue;
			}
//			remover pessoas do andar atual e adicionar no elevador
			if(elevador.getStatus() == Elevador.SUBIR){
				if (andarAtual < Orquestrador.NUM_ANDARES - 1){
					elevador.setAcao(Elevador.SUBIR);	
				}else{
					elevador.setAcao(Elevador.DESCER);
					elevador.setStatus(Elevador.DESCER);
				}
			}else{
				if (andarAtual > 0){
					elevador.setAcao(Elevador.DESCER);
				}else{
					elevador.setAcao(Elevador.SUBIR);
					elevador.setStatus(Elevador.SUBIR);
				}
			}
		}
		
	}

	private boolean alguemSair(int andarAtual,
			LinkedList<Pessoa> pessoasElevador) {
		for (Pessoa pessoa : pessoasElevador) {
			if(pessoa.getDestino() == andarAtual){
				return true;
			}
		}
		return false;
	}
}
