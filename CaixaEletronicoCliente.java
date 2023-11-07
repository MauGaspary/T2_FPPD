import java.rmi.Naming;
import java.util.Scanner;

public class CaixaEletronicoCliente {

    private static String codigo = "2";
    private static int count;
    
    public static String gerarCodigo(){
        String codCount = codigo + count;
        count++;
        return codCount;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            AdmInterface adm = (AdmInterface) Naming.lookup("rmi://localhost:1099/AdmService");

            int menu;
            do {
                System.out.println("1 - Autenticação de conta");
                System.out.println("2 - Sacar");
                System.out.println("3 - Depositar");
                System.out.println("4 - Consultar saldo");
                System.out.println("0 - Sair");
                int numeroConta;
                menu = scan.nextInt();
                switch (menu) {
                    case 1:
                        System.out.println("Digite o número da conta que deseja autenticar: ");
                        numeroConta = scan.nextInt();
                        if (adm.autenticarConta(numeroConta)) {
                            System.out.println("Conta existe e foi autenticada com sucesso!\n");
                        } else {
                            System.out.println("A conta não existe.\n");
                        }
                        break;
                    case 2:
                        System.out.println("Digite o número da conta que deseja sacar: ");
                        numeroConta = scan.nextInt();
                        System.out.println("Digite o valor do saque: ");
                        Double valor = scan.nextDouble();
                        if (adm.sacar(numeroConta, valor, codigo)) {
                            System.out.println("Saque realizado com sucesso!\n");
                        } else {
                            System.out.println("Saque não realizado. Verifique o saldo ou o número da conta.\n");
                        }
                        break;
                    case 3:
                        System.out.println("Digite o número da conta que deseja depositar: ");
                        numeroConta = scan.nextInt();
                        System.out.println("Digite o valor do depósito: ");
                        Double deposito = scan.nextDouble();
                        if (adm.depositar(numeroConta, deposito, codigo)) {
                            System.out.println("Depósito realizado com sucesso!\n");
                        } else {
                            System.out.println("Depósito não realizado. Verifique o número da conta.\n");
                        }
                        break;
                    case 4:
                        System.out.println("Digite o número da conta que deseja consultar o saldo: ");
                        numeroConta = scan.nextInt();
                        double saldo = adm.consultaSaldo(numeroConta);
                        if (saldo == -1.0) {
                            System.out.println("A conta não existe.\n");
                        } else {
                            System.out.println("O saldo da conta número " + numeroConta + " é: " + saldo + "\n");
                        }
                        break;
                    case 0:
                        System.out.println("Fechando o Programa.");
                        break;
                    default:
                        System.out.println("Digite uma opção válida!\n");
                }
            } while (menu != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scan.close();
    }
}
