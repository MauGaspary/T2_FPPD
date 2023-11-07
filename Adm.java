import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

class Conta {
    public int numeroConta;
    private double saldo;

    public Conta(int numeroConta) {
        this.numeroConta = numeroConta;
        this.saldo = 0.00;
    }

    public void setSaldo(double valor) {
        saldo = valor;
    }

    public double getSaldo() {
        return saldo;
    }
}

class Transacao {
    private String codTransacao;
    private boolean status;

    public Transacao(String codTransacao, boolean status) {
        this.codTransacao = codTransacao;
        this.status = status;
    }

    public String getCodTransacao() {
        return codTransacao;
    }

    public void setCodTransacao(String codTransacao) {
        this.codTransacao = codTransacao;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}

public class Adm extends UnicastRemoteObject implements AdmInterface {
    private Conta[] contas;
    private int numContas;
    private ArrayList<Transacao> transacoes;

    public Adm() throws RemoteException {
        this.contas = new Conta[10];
        transacoes = new ArrayList<Transacao>();
        for (int i = 0; i < 10; i++) {
            contas[i] = null;
        }
        numContas = 0;
    }

    public boolean containsTransacao(String codCount) {
        for (Transacao t : transacoes) {
            if (t.getCodTransacao() == codCount) {
                return true;
            }
        }
        return false;
    }

    public boolean getStatusTransacao(String codCount) {
        for (Transacao t : transacoes) {
            if (t.getCodTransacao() == codCount) {
                return t.getStatus();
            }
        }
        return false;
    }

    public Conta getConta(int numeroConta) {
        for (Conta c : contas) {
            if (c.numeroConta == numeroConta) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean autenticarConta(int numeroConta) throws RemoteException {
        for (Conta conta : contas) {
            if (conta != null && conta.numeroConta == numeroConta) {
                System.out.println("A conta número " + numeroConta + " existe.");
                return true;
            }
        }
        System.out.println("A conta número " + numeroConta + " não existe.");
        return false;
    }

    @Override
    public boolean criarConta(int numeroConta) throws RemoteException {
        if (numContas >= 10) {
            System.out.println("Número máximo de contas atingido.");
            return false;
        }

        for (Conta conta : contas) {
            if (conta != null && conta.numeroConta == numeroConta) {
                System.out.println("Número de conta já existente.");
                return false;
            }
        }
        Conta novaConta = new Conta(numeroConta);
        contas[numContas] = novaConta;
        System.out.println("Conta número " + numeroConta + " foi criada com sucesso.");
        numContas++;
        return true;
    }

    @Override
    public boolean fecharConta(int numeroConta) throws RemoteException {
        if (autenticarConta(numeroConta)) {
            int indiceConta = -1;
            for (int i = 0; i < contas.length; i++) {
                if (contas[i] != null && contas[i].numeroConta == numeroConta) {
                    indiceConta = i;
                    break;
                }
            }

            if (indiceConta != -1) {
                contas[indiceConta] = null;
                System.out.println("Conta número " + numeroConta + " foi fechada com sucesso.");
                numContas--;
                return true;
            }
        }
        System.out.println("A conta número " + numeroConta + " não existe.");
        return false;
    }

    @Override
    public double consultaSaldo(int numeroConta) throws RemoteException {
        if (autenticarConta(numeroConta)) {
            Conta conta = getConta(numeroConta);
            return conta.getSaldo();
        } else {
            return -1.0;
        }
    }

    @Override
    public boolean sacar(int numeroConta, double valor, String codTransacao) throws RemoteException {
        if (containsTransacao(codTransacao) && getStatusTransacao(codTransacao) == true) {
            return true;
        }
        if (autenticarConta(numeroConta)) {
            Conta c = getConta(numeroConta);
            if (valor > 0 && valor <= c.getSaldo()) {
                Double x = c.getSaldo() - valor;
                c.setSaldo(x);
                Transacao t = new Transacao(codTransacao, true);
                transacoes.add(t);
                System.out
                        .println("Saque de R$" + valor + " realizado com sucesso na conta número " + numeroConta + ".");
                System.out.println("O saldo da conta número " + numeroConta + " é: " + c.getSaldo());
                return true;
            } else {
                Transacao f = new Transacao(codTransacao, false);
                transacoes.add(f);
                System.out
                        .println("Saldo insuficiente na conta número " + numeroConta + " ou valor de saque inválido.");
                return false;
            }
        }
        System.out.println("A conta número " + numeroConta + " não existe.");
        return false;
    }

    @Override
    public boolean depositar(int numeroConta, double valor, String codTransacao) throws RemoteException {
        if (containsTransacao(codTransacao) && getStatusTransacao(codTransacao) == true) {
            return true;
        }
        if (autenticarConta(numeroConta)) {
            Conta c = getConta(numeroConta);
            if (valor > 0) {
                Double x = c.getSaldo() + valor;
                c.setSaldo(x);
                Transacao t = new Transacao(codTransacao, true);
                transacoes.add(t);
                return true;
            } else {
                Transacao f = new Transacao(codTransacao, false);
                transacoes.add(f);
                System.out.println("Valor de depósito inválido na conta número " + numeroConta + ".");
                return false;
            }
        } else {
            System.out.println("A conta número " + numeroConta + " não existe.");
            return false;
        }
    }

    @Override
    public boolean sacarFalso(int numeroConta, double valor, String codTransacao) throws RemoteException {
        if (containsTransacao(codTransacao) && getStatusTransacao(codTransacao) == true) {
            return true;
        }
        if (autenticarConta(numeroConta)) {
            Conta c = getConta(numeroConta);
            if (valor > 0 && valor <= c.getSaldo()) {
                Double x = c.getSaldo() - valor;
                c.setSaldo(x);
                Transacao t = new Transacao(codTransacao, true);
                transacoes.add(t);
                System.out
                        .println("Saque de R$" + valor + " realizado com sucesso na conta número " + numeroConta + ".");
                System.out.println("O saldo da conta número " + numeroConta + " é: " + c.getSaldo());
                return false;
            } else {
                Transacao f = new Transacao(codTransacao, false);
                transacoes.add(f);
                System.out
                        .println("Saldo insuficiente na conta número " + numeroConta + " ou valor de saque inválido.");
                return false;
            }
        }
        System.out.println("A conta número " + numeroConta + " não existe.");
        return false;
    }

    @Override
    public boolean depositarFalso(int numeroConta, double valor, String codTransacao) throws RemoteException {
        if (containsTransacao(codTransacao) && getStatusTransacao(codTransacao) == true) {
            return true;
        }
        if (autenticarConta(numeroConta)) {
            Conta c = getConta(numeroConta);
            if (valor > 0) {
                Double x = c.getSaldo() + valor;
                c.setSaldo(x);
                Transacao t = new Transacao(codTransacao, true);
                transacoes.add(t);
                return false;
            } else {
                Transacao f = new Transacao(codTransacao, false);
                transacoes.add(f);
                System.out.println("Valor de depósito inválido na conta número " + numeroConta + ".");
                return false;
            }
        } else {
            System.out.println("A conta número " + numeroConta + " não existe.");
            return false;
        }
    }
}