const urlParams = new URLSearchParams(window.location.search);
const roomName = urlParams.get('room') || 'default';
const userName = urlParams.get('user') || 'anonymous';

const socket = new WebSocket(`ws://localhost:8080/chats?room=${roomName}&user=${userName}`);

socket.addEventListener('open', (_) => {
  console.log('Connected to the WebSocket server');
});

socket.addEventListener('message', (event) => {
  const message = event.data;
  displayMessage(message, 'received');
});

socket.addEventListener('close', (_) => {
  console.log('Disconnected from the WebSocket server');
});
document.getElementById('message-input').addEventListener('keydown', (event) => {
  if (event.key === 'Enter') {
    document.getElementById('send-btn').click();
  }
});

document.getElementById('send-btn').addEventListener('click', () => {
  const messageInput = document.getElementById('message-input');
  const message = messageInput.value;
  if (message !== '') {
    socket.send(message);
    displayMessage(`You: ${message}`, 'sent');
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
