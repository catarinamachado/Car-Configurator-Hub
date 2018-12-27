package CCH.business;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.*;


public class ConfiguracaoOtima {

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

    private void restricaoPacotes(IloCplex cplex, Collection<Pacote> pacotes, Collection<Componente> componentes, HashMap<Integer, IloIntVar> comps, HashMap<Integer, IloIntVar> pacs) throws IloException{
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
        restricaoPacotes(cplex,pacotes,componentes,comps,pacs);

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