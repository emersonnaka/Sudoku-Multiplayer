# Sudoku Multiplayer

## Jogo: Sudoku multiplayer
O jogo utiliza comunicação multicast e udp.
* Quando um player entra, ele manda mensagem no grupo e cada participante do grupo responde pelo udp
* As mensagens enviadas pelo chat são enviadas pelo multicas
* Cada célula do sudoku envia mensagens automaticamente:
	* Seleciona a célula para inserir um número: todos os outros participantes tem a célula bloqueada para que dois participantes ou mais editem a mesma célula
	* Quando um número é inserido, uma mensagem é enviada com a posição [x,y] da célula e o número inserido.
	* Ao sair da célula, outra mensagem é enviada para que essa célula seja liberada no Sudoku dos outros jogadores
	* Quando algum jogador consegue resolver o Sudoku é enviado uma mensagem
	* Quando o jogador sair do jogo, todos os usuários recebem a mensagem informando que ele saiu do jogo.