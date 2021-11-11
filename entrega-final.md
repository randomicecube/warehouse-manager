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

## Coisas que faltam fazer

- [DONE] - falta a lógica de dar update aos Status (e a lógica de dar update aos pontos) também
- [DONE] - tratar dos métodos do saldo -> apresentar accounting balance e etc
- [DONE] - delivery methods de notificações -> strategy
- [DONE] - mudar pontos de int para double

# TODO LIST FINAL

- [DONE] Ver se os imports estão bem (nada importado a mais)
- [DONE] Adicionar constantes de serialização a tudo o que ainda não as tem
- [KINDA?] Confirmar se "vendas pagas" inclui desagregação (no Partner)
- [DONE] Verificar se é mesmo preciso addProduct e/ou addTransaction (Warehouse)
- [DONE] UpdateBalanceSale -> preciso? Como é que está o da Breakdown?
- [DONE] Confirmar se as batches estão a ser removidas na desagregação -> adicionado na breakdownProcedure
- [DONE] Comment cleanup -> principalmente WHM e Exceptions
- [DONE] Cleanup smallest e biggest, transformar numa função
- [DONE] General code cleanup
- [DONE] Update points no breakdown -> importante, dá update aos pontos só, sem contar com multas
- [DONE] Rever FIXME's e TODO's no código
- [DONE] Unmodifiable collections/cópias -> importante
- [DONE] Diamond recursion na verificação da desagregação
- [DONE] Visitor em vez de HasRecipe
- [DONE] getPaymentsByPartner (WH) -> o partner não tem já um método com isso?
- [DONE] Confirmar se todos os métodos são utilizados
- [DONE] Alterar a ordem dos métodos, para ser mais consistente (e de acesso mais intuitivo)
- [DONE] Remover o máximo de casts possível -> removeu-se todos pog
- [DONE] Dividir linhas muito longas
- [DONE] Verificar se no WHM tudo tem a saveFlag
- [DONE] COMMIT NO CVS!!!!!