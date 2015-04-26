import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

/** 
 * Para cada elevador verificar acao, se subir, continuar subindo, 
 * chegando no último andar, descer. Se descer, continuar descendo, chegando 
 * no primeiro andar, subir.
 */

public class Dummy extends Orquestrador {

	public Dummy() {
		super();
	}

	public void tomarDecisoes(){
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
			// elevador para caso alguem sobe ou desce
			if (elevador.alguemSai() || elevador.alguemEntra()){
				  elevador.setAcao(Elevador.PARAR);
				  continue;
			}
			// se em movimento entao continua em movimento
			if(elevador.getStatus() == Elevador.SUBIR){
				if (andarAtual < elevador.getTeto()){
					elevador.setAcao(Elevador.SUBIR);	
				}else{
					elevador.setAcao(Elevador.DESCER);
					elevador.setStatus(Elevador.DESCER);
				}
			}else{
				if (andarAtual >  elevador.getChao()){
					elevador.setAcao(Elevador.DESCER);
				}else{
					elevador.setAcao(Elevador.SUBIR);
					elevador.setStatus(Elevador.SUBIR);
				}
			}
		}
		
	}
}
