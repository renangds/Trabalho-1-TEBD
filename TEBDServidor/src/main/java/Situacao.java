public enum Situacao {
    EM_PROCESSO(1), APP_SELECIONADO(2),
    APP_ESPERA(3), REP(4);

    private int numeroCondicao;

    Situacao(int numeroCondicao){
        this.numeroCondicao = numeroCondicao;
    }

    public int getNumeroCondicao() {
        return numeroCondicao;
    }
}
