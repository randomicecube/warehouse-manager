# Funcionalidade a implementar - Entrega final

## Menu de Gestão de Produtos

- [DONE] Visualizar lotes fornecidos por parceiro
- [DONE] Visualizar lotes de um dado produto

## Menu de Gestão de Parceiros

- [DONE] Ativar/Desativar notificações de um produto
- [DONE] Mostrar compras com parceiro
- [DONE] Mostrar vendas/desagregações com parceiro

## Menu de Gestão de Transações

- [DONE] Visualizar Transação
- Registar desagregação
- Registar venda
- [DONE] Registar compra
- [DONE] Receber pagamento de venda

## Menu de Consultas

- [DONE] Produtos com preço abaixo de limite
- [DONE] Faturas pagas por parceiro

- [DONE] - falta a lógica de dar update aos Status (e a lógica de dar update aos pontos) também
- [DONE] - tratar dos métodos do saldo -> apresentar accounting balance e etc
- [DONE] - delivery methods de notificações -> strategy
- [DONE] - mudar pontos de int para double

## TODO LIST FINAL

- [DONE] Ver se os imports estão bem (nada importado a mais)
- [DONE] Adicionar constantes de serialização a tudo o que ainda não as tem
- Confirmar se "vendas pagas" inclui desagregação (no Partner)
- [DONE] Verificar se é mesmo preciso addProduct e/ou addTransaction (Warehouse)
- UpdateBalanceSale -> preciso? Como é que está o da Breakdown?
- Confirmar se as batches estão a ser removidas na desagregação
- Comment cleanup -> principalmente WHM e Exceptions
- [DONE] Cleanup smallest e biggest, transformar numa função
- General code cleanup
- Update points no breakdown -> importante, dá update aos pontos só, sem contar com multas
- [DONE] Rever FIXME's e TODO's no código
- [DONE] Unmodifiable collections/cópias -> importante
- Diamond recursion na verificação da desagregação
- getPaymentsByPartner (WH) -> o partner não tem já um método com isso?
- Confirmar se todos os métodos são utilizados
- Alterar a ordem dos métodos, para ser mais consistente (e de acesso mais intuitivo)
- [DONE] Remover o máximo de casts possível
- Dividir linhas muito longas