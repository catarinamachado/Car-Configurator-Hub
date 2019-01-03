package CCH.business;

import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

import ilog.concert.*;
import ilog.cplex.IloCplex;

/**
 * Classe que gera uma configuração ótima para um determinado valor máximo.
 *
 * @version 20181229
 */

public class ConfiguracaoOtima {

    /**
     * Método que constrói as restrições relativas aos componentes.
     *
     * @param cplex Modelo que está a ser construído
     * @param componentesObrigatorios Componentes que fazem parte da
     * configuração que se pretende otimizar
     * @param componentes Todos os componentes presentes no sistema
     * @param comps HashMap com o id do componente como chave e a variável de decisão como valor
     * @throws IloException Caso a restrição que se pretende adicionar ao modelo
     * não vá de encontro ao CPLEX
     */
    private void restricaoComponentes(IloCplex cplex, Collection<Componente> componentesObrigatorios, Collection<Componente> componentes, HashMap<Integer, IloIntVar> comps) throws IloException {
        for (Componente c : componentes) {
            IloIntVar value = comps.get(c.getId());
            Set<Integer> incomp = c.getIncompativeis().keySet();

            for (Integer k : incomp) {
                if (comps.containsKey(k) && c.getId() < k) {
                    IloIntVar kvalue = comps.get(k);
                    cplex.addLe(cplex.sum(value,kvalue),1);
                }
            }

            Set<Integer> necessarios = c.getRequeridos().keySet();

            for (Integer k:necessarios) {
                if (comps.containsKey(k)) {
                    IloIntVar kvalue = comps.get(k);
                    cplex.addLe(value,kvalue);//se a precisa de b entao a<=b
                } else {
                    cplex.addEq(0, value);//se o componente não estiver em comps o modelo não o seleciona, Para prevenir Erros.
                }
            }
        }
        //componentes obrigatórios
        for (Componente c:componentesObrigatorios) {
            if(!comps.containsKey(c.getId())) {
                throw new IloException();
            }

            cplex.addEq(comps.get(c.getId()),1);
            //garante que os componentes obrigatórios são selecionados
            //está aqui para garantir que não ocorrem incompatibilidades
        }
    }

    /**
     * Método que constrói as restrições relativas aos pacotes.
     *
     * @param cplex Modelo que está a ser construído
     * @param pacotes Todos os pacotes presentes no sistema
     * @param comps HashMap com o id do componente como chave e a variável de decisão como valor
     * @param pacs HashMap com o id do pacote como chave e a variável de decisão como valor
     * @throws IloException Caso a restrição que se pretende adicionar ao modelo
     * não vá de encontro ao CPLEX
     */
    private void restricaoPacotes(IloCplex cplex, Collection<Pacote> pacotes, HashMap<Integer, IloIntVar> comps, HashMap<Integer, IloIntVar> pacs) throws IloException{
        HashMap<Integer,IloNumExpr> overlap= new HashMap<>();

        for (Pacote p : pacotes) {
            IloIntVar pvalue = pacs.get(p.getId());
            Set<Integer> cs = p.getComponentes().keySet();

            for (Integer k:cs) {
                if (comps.containsKey(k)) {
                    IloIntVar kvalue = comps.get(k);
                    cplex.addLe(pvalue,kvalue);//se a precisa de b entao a<=b
                    // construção das restrições de overlap de pacotes

                    if(!overlap.containsKey(k)) {
                        overlap.put(k,cplex.constant(0));
                    }

                    IloNumExpr exp = overlap.get(k);
                    exp = cplex.sum(exp,pvalue);
                    overlap.put(k,exp);
                } else {
                    cplex.addEq(0,pvalue);//se o componente não estiver em comps o modelo não o seleciona, Para prevenir Erros.
                }
            }
        }
        for (IloNumExpr exp:overlap.values())
            cplex.addLe(exp,1);//apenas um pacote pode conter um componente
    }

    /**
     * Método que gera a configuração ótima, ou seja, uma configuração que tenta
     * maximizar a utilização do dinheiro previsto. Este método constrói o modelo
     * de otimização de programação inteira recorrendo ao CPLEX para obter a
     * solução.
     *
     * @param componentesObrigatorios Componentes que já faziam parte da
     * configuração que se pretende otimizar
     * @param componentes Todos os componentes presentes no sistema
     * @param pacotes Todos os pacotes presentes no sistema
     * @param money Valor máximo disponível
     * @return Configuracao ótima gerada
     * @throws IloException Caso não exista solução para o modelo gerado
     */
    public Configuracao configuracaoOtima(
            Collection<Componente> componentesObrigatorios,
            Collection<Componente> componentes,
            Collection<Pacote> pacotes,
            double money
    ) throws IloException {

        IloCplex cplex = new IloCplex();
        HashMap<Integer,IloIntVar> comps = new HashMap<>();
        HashMap<Integer,IloIntVar> pacs = new HashMap<>();

        IloNumExpr compsPreco = cplex.constant(0);
        IloNumExpr pacsDesconto = cplex.constant(0);

        //Variaveis de decisão
        for (Componente c:componentes) {
            int ckey = c.getId();
            IloIntVar cvalue = cplex.boolVar("c"+ckey);
            comps.put(ckey,cvalue);
            compsPreco = cplex.sum(compsPreco,cplex.prod(cvalue,c.getPreco()));
        }
        for (Pacote p:pacotes) {
            int pkey = p.getId();
            IloIntVar pvalue = cplex.boolVar("p"+pkey);
            pacs.put(pkey,pvalue);
            pacsDesconto = cplex.sum(pacsDesconto,cplex.prod(pvalue, p.getDesconto()));
        }

        //Função Objetivo
        cplex.addMaximize(compsPreco);

        //restrições de pacotes e componentes
        restricaoComponentes(cplex,componentesObrigatorios,componentes,comps);
        restricaoPacotes(cplex,pacotes,comps,pacs);

        //restrição de preço
        cplex.addLe(cplex.sum(compsPreco,cplex.prod(-1,pacsDesconto)),money);

        //resolve o problema
        if(cplex.solve()) {
            //retornar configuração
            ArrayList<Pacote> pacotesAceitados = new ArrayList<>();
            for (Pacote p: pacotes) {
                IloIntVar pvar = pacs.get(p.getId());
                if (cplex.getValue(pvar)==1)
                    pacotesAceitados.add(p);
            }
            ArrayList<Componente> componentesAceitados = new ArrayList<>();
            for (Componente c: componentes) {
                IloIntVar cvar = comps.get(c.getId());
                if(cplex.getValue(cvar) == 1)
                    componentesAceitados.add(c);
            }
            Configuracao otima = new Configuracao(pacotesAceitados,componentesAceitados);
            
            return otima;
        }
        else throw new IloException();
    }
}