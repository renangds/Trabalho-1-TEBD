public class Candidato {
    private Situacao situacao;
    private String nome;
    private String universidade;
    private String cpf;
    private float cr;

    public Candidato(String nome, String universidade, String cpf, float cr){
        this.nome = nome;
        this.universidade = universidade;
        this.cpf = cpf;
        this.cr = cr;

        System.out.println("O cr é:" + cr);

        if(cr < 5.0){
            this.situacao = Situacao.REP;
        } else if (cr <= 6.5){
            this.situacao = Situacao.APP_SELECIONADO;
        } else if (cr <= 7.5) {
            this.situacao = Situacao.APP_ESPERA;
        } else{
            this.situacao = Situacao.APP_SELECIONADO;
        }
    }

    public void inserirSituacao(Situacao situacao){
        this.situacao = situacao;
    }

    public int getSituacao(){
        return this.situacao.getNumeroCondicao();
    }

    public String getCpf() { return this.cpf; }
}
