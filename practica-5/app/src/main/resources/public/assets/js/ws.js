let socket

document.getElementById('chat-btn').addEventListener('click', () => {
  const chatList = document.getElementById('chat-list');
  const closeChatList = document.getElementById('close-chat-list');
  const chatWindow = document.getElementById('chat-window');
  const closeChat = document.getElementById('close-chat');
  const chatBtn = document.getElementById('chat-btn');

  // Determinar el destinatario del chat y el nombre de la sala (roomName)
  const chatRecipient = document.getElementById('article-author')
    ? document.getElementById('article-author').textContent.trim()
    : 'admin';

  // Verificar si el usuario está en localStorage
  let username = localStorage.getItem('username')
    || document.getElementById('thymeleaf-username')?.textContent.trim()
    || null;



  // Si no hay usuario, pedir el nombre
  if (!username) {
    const enteredName = prompt('Por favor, introduce tu nombre para iniciar el chat:');
    if (enteredName) {
      username = enteredName.trim();
      localStorage.setItem('username', username);
    } else {
      return; // Si no se ingresa un nombre, no abre el chat
    }
  }
  const roomName = `${chatRecipient}_${username}`  // Sala basada en el autor (para `article_view.html`)

  console.log(`username: ${username}`);
  console.log(`chat recipient: ${chatRecipient}`);
  console.log(`roomName: ${roomName}`);

  // Abrir la ventana del chat
  if (!localStorage.getItem('username')) {
    chatList.style.display = 'block';
    chatWindow.style.display = 'none';

    // Cargar los chats solo una vez
    loadChats(username);

    // Delegación de eventos en el contenedor principal
    document.addEventListener('click', (e) => {
      const chat = e.target.closest('.chat-element');  // Verifica el click en un chat

      if (chat) {
        e.preventDefault();
        const span = chat.querySelector('span');

        if (span) {
          chatList.style.display = 'none';
          chatWindow.style.display = 'block';
          const chatname = span.textContent.trim();
          loadMessagesByUsername(username, chatname);
        } else {
          console.warn('No se encontró el span o el atributo data-room.');
        }
      }
    });
  } else {
    chatList.style.display = 'none';
    chatWindow.style.display = 'block';
    loadChatHistory(username, roomName);
  }

  chatBtn.style.display = 'none';
  closeChatList.addEventListener('click', () => {
    document.getElementById('chat-list-container').innerHTML = '';
    chatList.style.display = 'none';
    chatBtn.style.display = 'block';
  });

  closeChat.addEventListener('click', () => {
    if (socket && socket.readyState !== WebSocket.CLOSED) {
      document.getElementById('chat-box').innerHTML = '';
      document.getElementById('chat-list-container').innerHTML = '';
      socket.close();
      chatWindow.style.display = 'none';
      chatBtn.style.display = 'block';
    }
  });

  // Crear conexión WebSocket incluyendo el roomName
  socket = new WebSocket(`ws://localhost:8080/chats?user=${username}&room=${roomName}`);
  console.log(`Intentando conectar con: ws://localhost:8080/chats?user=${username}&room=${roomName}`);

  socket.addEventListener('open', () => {
    //loadChatHistory(username, roomName);
    //console.log(`Connected as ${username} - Chatting in room: ${roomName}`);
  });

  socket.addEventListener('message', (event) => {
    try {
      const isJSON = event.data.trim().startsWith('{') && event.data.trim().endsWith('}');

      if (isJSON) {
        const messageData = JSON.parse(event.data);
        displayMessage(`${messageData.sender}: ${messageData.message}`, 'received');
      } else {
        // Si el mensaje no es JSON, mostrarlo como texto plano
        displayMessage(`${event.data}`, 'received');
      }
    } catch (error) {
      console.error('Error procesando mensaje del WebSocket:', error);
    }
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
        console.log(`recipient: ${chatRecipient}`);
        socket.send(JSON.stringify({
          sender: username,
          recipient: chatRecipient,
          room: roomName,
          message: message
        }));
        displayMessage(`${message}`, 'sent');
      } else {
        console.error('WebSocket no está en estado OPEN');
      }
      messageInput.value = '';
    }
  });

});

function displayMessage(message, type) {
  const chatBox = document.getElementById('chat-box');
  const messageElement = document.createElement('div');
  messageElement.className = `chat-message ${type}`;
  messageElement.textContent = message;
  chatBox.appendChild(messageElement);
  chatBox.scrollTop = chatBox.scrollHeight;
}

function displayChats(chatName, room) {
  const chatListContainer = document.getElementById('chat-list-container');
  const chatsElement = document.createElement('div');
  chatsElement.className = `chat d-flex justify-content-between align-items-center p-3 mb-2 rounded shadow-sm bg-dark`;
  chatsElement.innerHTML = `
    <a href='#' class='chat-element'>
      <span class="chat-name" data-room=${room} style="font-weight: bold; color: white; font-size: 1.1rem;">${chatName}</span>
    </a>
  `;
  chatListContainer.appendChild(chatsElement);
  chatListContainer.scrollTop = chatListContainer.scrollHeight;
}

function loadChats(username) {
  fetch(`http://localhost:8080/chats/${username}`)
    .then(response => response.json())
    .then(data => {
      const chats = new Map();
      data.forEach(chat => {
        if (!chats.has(chat.chatName)) {
          chats.set(chat.chatName, { chatname: chat.chatName, room: chat.room });
        }
      });

      chats.forEach(chat => {
        console.log('Chat:', chat);
        displayChats(chat.chatname, chat.room);
      });
    })
    .catch(error => {
      console.error('Error cargando el historial:', error);
    });
}
function loadMessagesByUsername(username, chatname) {
  fetch(`http://localhost:8080/chats/${username}`)
    .then(response => response.json())
    .then(data => {
      data.forEach(chat => {
        if (chat.chatName === chatname) {
          const messageType = chat.heWroteIt ? 'sent' : 'received';
          displayMessage(`${chat.message}`, messageType);
        }
      });
    })
    .catch(error => {
      console.error('Error cargando el historial:', error);
    });
}
// ✅ CORRECCIÓN: Mueve esta función fuera del bloque principal
function loadChatHistory(username, room) {
  fetch(`http://localhost:8080/api/chats/history?user=${username}&&room=${room}`)
    .then(response => response.json())
    .then(data => {
      data.forEach(message => {
        const messageType = message.sender === username ? 'sent' : 'received';
        displayMessage(`${message.message}`, messageType);
        console.log(message);
      });
    })
    .catch(error => {
      console.error('Error cargando el historial:', error);
    });
}
