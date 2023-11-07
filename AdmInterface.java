import java.rmi.*;

public interface AdmInterface extends Remote{
    public boolean autenticarConta(int numeroConta) throws RemoteException;
    public boolean criarConta(int numeroConta) throws RemoteException;
    public boolean fecharConta(int numeroConta) throws RemoteException;
    public double consultaSaldo(int numeroConta) throws RemoteException;
    public boolean sacar(int numeroConta, double valor, String codTransacao) throws RemoteException;
    public boolean depositar(int numeroConta, double valor, String codTransacao) throws RemoteException;
    public boolean sacarFalso(int numeroConta, double valor, String codTransacao) throws RemoteException;
    public boolean depositarFalso(int numeroConta, double valor, String codTransacao) throws RemoteException;

}