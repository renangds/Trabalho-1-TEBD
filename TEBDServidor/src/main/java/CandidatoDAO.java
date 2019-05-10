import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {
    private List<Candidato> candidatos;

    public CandidatoDAO(){
        this.candidatos = new ArrayList<Candidato>();

        this.candidatos.add((new Candidato("Fulano", "UFRJ", "111.111.111-52", Float.parseFloat("5.5"))));
        this.candidatos.add((new Candidato("Beltrano", "UFF", "222.222.222-52", Float.parseFloat("7"))));
        this.candidatos.add((new Candidato("Ciclano", "GREVERJ", "333.333.333-52", Float.parseFloat("8.0"))));
    }

    public int addCandidato(String nome, String universidade, String cpf, float cr){
        int situacao = this.buscaCandidato(cpf);

        if(situacao == 0){
            Candidato candidato = new Candidato(nome, universidade, cpf, cr);
            this.candidatos.add(candidato);

            return 0;
        } else{
            return -1;
        }
    }

    public int buscaCandidato(String cpf){

        for (Candidato candidato : this.candidatos) {
            if(candidato.getCpf().equals(cpf)) return candidato.getSituacao();
        }

        return 0;
    }
}
