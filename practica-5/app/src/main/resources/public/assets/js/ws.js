let socket = null; // Variable global para manejar la conexión WebSocket

document.getElementById('chat-btn').addEventListener('click', () => {
  const chatWindow = document.getElementById('chat-window');

  // Determinar el destinatario del chat y el nombre de la sala (roomName)
  const chatRecipient = document.getElementById('article-author')
      ? document.getElementById('article-author').textContent.trim()
      : 'admin';

  const user = localStorage.getItem('username')
      || document.getElementById('thymeleaf-username')?.textContent.trim()
      || null;

  const roomName = document.getElementById('article-author')
      ? `${chatRecipient}_${user}`  // Sala basada en el autor (para `article_view.html`)
      : `admin_${user}`;             // Sala predeterminada para el chat del administrador

  // Verificar si el usuario está en localStorage
  let userName = localStorage.getItem('username')
      || document.getElementById('thymeleaf-username')?.textContent.trim()
      || null;

  // Si no hay usuario, pedir el nombre
  if (!userName) {
    const enteredName = prompt('Por favor, introduce tu nombre para iniciar el chat:');
    if (enteredName) {
      userName = enteredName.trim();
      localStorage.setItem('username', userName);
    } else {
      return; // Si no se ingresa un nombre, no abre el chat
    }
  }

  console.log(`username: ${userName}`);
  console.log(`chat recipient: ${chatRecipient}`);
  console.log(`roomName: ${roomName}`);

  // Abrir la ventana del chat
  chatWindow.style.display = 'block';

  // Cerrar conexión previa si ya existe
  if (socket !== null && socket.readyState !== WebSocket.CLOSED) {
    socket.close();
  }

  // Crear conexión WebSocket incluyendo el roomName
  socket = new WebSocket(`ws://localhost:8080/chats?user=${userName}&room=${roomName}`);
  console.log(`Intentando conectar con: ws://localhost:8080/chats?user=${userName}&room=${roomName}`);

  socket.addEventListener('open', () => {
    console.log(`Connected as ${userName} - Chatting in room: ${roomName}`);
  });

  socket.addEventListener('message', (event) => {
    const message = event.data;
    displayMessage(message.message, 'received');
  });

  socket.addEventListener('close', () => {
    console.log('Disconnected from the WebSocket server');
  });

  document.getElementById('message-input').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
      document.getElementById('send-btn').click();
    }
  });

  document.getElementById('send-btn').addEventListener('click', () => {
    const messageInput = document.getElementById('message-input');
    const message = messageInput.value.trim();
    if (message !== '') {
      if (socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify({
          sender: userName,
          recipient: chatRecipient,
          room: roomName,
          message: message
        }));
        displayMessage(`You: ${message}`, 'sent');
      } else {
        console.error('WebSocket no está en estado OPEN');
      }
      messageInput.value = '';
    }
  });

  function displayMessage(message, type) {
    const chatBox = document.getElementById('chat-box');
    const messageElement = document.createElement('div');
    messageElement.className = `chat-message ${type}`;
    messageElement.textContent = message;
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
  }
});

document.getElementById('close-chat').addEventListener('click', () => {
  if (socket) {
    socket.close();
  }
  document.getElementById('chat-window').style.display = 'none';
});
