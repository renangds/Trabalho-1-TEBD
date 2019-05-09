import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {
    private List<Candidato> candidatos;

    public CandidatoDAO(){
        this.candidatos = new ArrayList<Candidato>();
    }

    public int addCandidato(String nome, String universidade, String cpf, float cr){
        int situacao = this.buscaCandidato(cpf);

        if(situacao == 0){
            Candidato candidato = new Candidato(nome, universidade, cpf, cr);
            this.candidatos.add(candidato);

            return 1;
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
