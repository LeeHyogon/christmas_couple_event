let memoriesLoaded = false; // 추억 목록이 로드되었는지 확인
let messagesLoaded = false;
let photosLoaded = false;  // 사진 로드 상태 추가
let currentMessageAction = null; // 'edit' or 'delete'
let currentMessageId = null;

document.addEventListener('DOMContentLoaded', function() {
    // 커플 정보 저장
    const coupleForm = document.getElementById('coupleForm');
    
    if (coupleForm) {
        coupleForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const partner1Name = document.getElementById('partner1Name').value;
            const partner2Name = document.getElementById('partner2Name').value;
            const anniversaryDate = document.getElementById('anniversaryDate').value;
            const description = document.getElementById('description').value;
            const photoFile = document.getElementById('couplePhoto').files[0];
            
            const formData = new FormData();
            formData.append('partner1Name', partner1Name);
            formData.append('partner2Name', partner2Name);
            formData.append('anniversaryDate', anniversaryDate);
            if (description) {
                formData.append('description', description);
            }
            if (photoFile) {
                formData.append('photo', photoFile);
            }
            
            try {
                const response = await fetch('/api/couple', {
                    method: 'POST',
                    body: formData
                });
                
                if (response.ok) {
                    alert('커플 정보가 저장되었습니다! 💕');
                    location.reload();
                } else {
                    alert('저장에 실패했습니다. 다시 시도해주세요.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            }
        });
    }
    
    // 추억 작성/수정 폼
    const memoryForm = document.getElementById('memoryForm');
    if (memoryForm) {
        memoryForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            await saveMemory();
        });
    }
    
    // 메시지 작성/수정 폼
    const messageForm = document.getElementById('messageForm');
    if (messageForm) {
        messageForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            await saveMessage();
        });
    }
    
    // 비밀번호 폼
    const passwordForm = document.getElementById('passwordForm');
    if (passwordForm) {
        passwordForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            await handleMessagePassword();
        });
    }
    
    // 사진 업로드 폼
    const photoForm = document.getElementById('photoForm');
    if (photoForm) {
        photoForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            await uploadPhoto();
        });
    }
    
    // 크리스마스 카운트다운
    const christmasDate = new Date(new Date().getFullYear(), 11, 25);
    const today = new Date();
    
    if (today.getMonth() === 11 && today.getDate() > 25) {
        christmasDate.setFullYear(christmasDate.getFullYear() + 1);
    }
    
    const diffTime = christmasDate - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays > 0) {
        console.log(`크리스마스까지 ${diffDays}일 남았습니다!`);
    }
});

// 메시지 섹션으로 스크롤 이동 및 메시지 로드
function scrollToMessages() {
    const messagesSection = document.getElementById('messagesSection');
    
    messagesSection.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });
    
    if (!messagesLoaded) {
        setTimeout(() => {
            loadMessages();
        }, 300);
    }
}

// 추억 섹션으로 스크롤 이동 및 추억 로드
function scrollToMemories() {
    const memoriesSection = document.getElementById('memoriesSection');
    
    memoriesSection.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });
    
    if (!memoriesLoaded) {
        setTimeout(() => {
            loadMemories();
        }, 300);
    }
}

// 메시지 목록 불러오기
async function loadMessages() {
    if (messagesLoaded) {
        return;
    }
    
    const messagesList = document.getElementById('messagesList');
    const loading = document.getElementById('messagesLoading');
    
    loading.style.display = 'block';
    messagesList.innerHTML = '';
    
    try {
        const response = await fetch('/api/messages/public');
        const messages = await response.json();
        
        messagesLoaded = true;
        loading.style.display = 'none';
        
        if (messages.length === 0) {
            messagesList.innerHTML = '<p class="no-messages">아직 메시지가 없습니다. 첫 메시지를 작성해보세요! 💌</p>';
            return;
        }
        
        messages.forEach(message => {
            const messageCard = createMessageCard(message);
            messagesList.appendChild(messageCard);
        });
    } catch (error) {
        console.error('메시지 목록 로드 실패:', error);
        loading.style.display = 'none';
        messagesList.innerHTML = '<p class="no-messages">메시지를 불러오는데 실패했습니다. 다시 시도해주세요.</p>';
    }
}

// 메시지 카드 생성
function createMessageCard(message) {
    const card = document.createElement('div');
    card.className = 'message-card';
    card.dataset.id = message.id;
    
    const createdAt = new Date(message.createdAt);
    const formattedDate = createdAt.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
    
    card.innerHTML = `
        <div class="message-header">
            <div class="message-author">✉️ ${escapeHtml(message.author)}</div>
            <div class="message-actions">
                <button class="edit-btn" onclick="editMessage(${message.id})">✏️</button>
                <button class="delete-btn" onclick="deleteMessage(${message.id})">🗑️</button>
            </div>
        </div>
        <div class="message-title">${escapeHtml(message.title)}</div>
        <div class="message-content">${escapeHtml(message.content)}</div>
        <div class="message-footer">
            <span class="message-date">${formattedDate}</span>
            ${message.isPublic ? '<span class="public-badge">공개</span>' : '<span class="private-badge">비공개</span>'}
        </div>
    `;
    
    return card;
}

// 메시지 저장 (생성/수정)
async function saveMessage() {
    const id = document.getElementById('messageId').value;
    const title = document.getElementById('messageTitle').value;
    const author = document.getElementById('messageAuthor').value;
    const content = document.getElementById('messageContent').value;
    const isPublic = document.getElementById('messageIsPublic').checked;
    const password = document.getElementById('messagePassword').value;
    
    const data = {
        title: title,
        author: author,
        content: content,
        isPublic: isPublic,
        password: password || null
    };
    
    try {
        const url = id ? `/api/messages/${id}` : '/api/messages';
        const method = id ? 'PUT' : 'POST';
        
        // 수정 시 비밀번호가 필요할 수 있음
        const passwordParam = id && password ? `?password=${encodeURIComponent(password)}` : '';
        const finalUrl = id ? `${url}${passwordParam}` : url;
        
        const response = await fetch(finalUrl, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            alert('메시지가 저장되었습니다! 💌');
            closeMessageForm();
            messagesLoaded = false;
            loadMessages();
        } else {
            const errorText = await response.text();
            alert('저장에 실패했습니다: ' + errorText);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// 메시지 수정
async function editMessage(id) {
    try {
        const response = await fetch(`/api/messages/${id}`);
        const message = await response.json();
        
        document.getElementById('messageId').value = message.id;
        document.getElementById('messageTitle').value = message.title;
        document.getElementById('messageAuthor').value = message.author;
        document.getElementById('messageContent').value = message.content;
        document.getElementById('messageIsPublic').checked = message.isPublic;
        document.getElementById('messageModalTitle').textContent = '메시지 수정';
        
        openMessageForm();
    } catch (error) {
        console.error('Error:', error);
        alert('메시지를 불러오는데 실패했습니다.');
    }
}

// 메시지 삭제
async function deleteMessage(id) {
    currentMessageAction = 'delete';
    currentMessageId = id;
    openPasswordModal();
}

// 비밀번호 처리
async function handleMessagePassword() {
    const password = document.getElementById('passwordInput').value;
    
    if (currentMessageAction === 'delete') {
        try {
            const response = await fetch(`/api/messages/${currentMessageId}?password=${encodeURIComponent(password)}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                alert('메시지가 삭제되었습니다.');
                closePasswordModal();
                messagesLoaded = false;
                loadMessages();
            } else {
                alert('비밀번호가 일치하지 않거나 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        }
    }
}

// 메시지 모달 열기
function openMessageForm() {
    document.getElementById('messageModal').style.display = 'block';
    document.getElementById('messageForm').reset();
    document.getElementById('messageId').value = '';
    document.getElementById('messageModalTitle').textContent = '새 메시지 작성';
}

// 메시지 모달 닫기
function closeMessageForm() {
    document.getElementById('messageModal').style.display = 'none';
    document.getElementById('messageForm').reset();
    document.getElementById('messageId').value = '';
}

// 비밀번호 모달 열기
function openPasswordModal() {
    document.getElementById('messagePasswordModal').style.display = 'block';
    document.getElementById('passwordInput').value = '';
}

// 비밀번호 모달 닫기
function closePasswordModal() {
    document.getElementById('messagePasswordModal').style.display = 'none';
    document.getElementById('passwordInput').value = '';
    currentMessageAction = null;
    currentMessageId = null;
}

// 추억 목록 불러오기
async function loadMemories() {
    if (memoriesLoaded) {
        return;
    }
    
    const memoriesList = document.getElementById('memoriesList');
    const loading = document.getElementById('memoriesLoading');
    
    loading.style.display = 'block';
    memoriesList.innerHTML = '';
    
    try {
        const response = await fetch('/api/memories');
        const memories = await response.json();
        
        memoriesLoaded = true;
        loading.style.display = 'none';
        
        if (memories.length === 0) {
            memoriesList.innerHTML = '<p class="no-memories">아직 추억이 없습니다. 첫 추억을 작성해보세요! 💕</p>';
            return;
        }
        
        memories.forEach(memory => {
            const memoryCard = createMemoryCard(memory);
            memoriesList.appendChild(memoryCard);
        });
    } catch (error) {
        console.error('추억 목록 로드 실패:', error);
        loading.style.display = 'none';
        memoriesList.innerHTML = '<p class="no-memories">추억을 불러오는데 실패했습니다. 다시 시도해주세요.</p>';
    }
}

// 추억 카드 생성
function createMemoryCard(memory) {
    const card = document.createElement('div');
    card.className = 'memory-card';
    card.dataset.id = memory.id;
    
    const date = new Date(memory.memoryDate);
    const formattedDate = date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
    
    card.innerHTML = `
        <div class="memory-header">
            <h3>${escapeHtml(memory.title)}</h3>
            <div class="memory-actions">
                <button class="edit-btn" onclick="editMemory(${memory.id})">✏️</button>
                <button class="delete-btn" onclick="deleteMemory(${memory.id})">🗑️</button>
            </div>
        </div>
        <div class="memory-date">📅 ${formattedDate}</div>
        ${memory.location ? `<div class="memory-location">📍 ${escapeHtml(memory.location)}</div>` : ''}
        ${memory.content ? `<div class="memory-content">${escapeHtml(memory.content)}</div>` : ''}
    `;
    
    return card;
}

// 추억 저장 (생성/수정)
async function saveMemory() {
    const id = document.getElementById('memoryId').value;
    const title = document.getElementById('memoryTitle').value;
    const memoryDate = document.getElementById('memoryDate').value;
    const location = document.getElementById('memoryLocation').value;
    const content = document.getElementById('memoryContent').value;
    const displayOrder = parseInt(document.getElementById('memoryDisplayOrder').value) || 0;
    
    const data = {
        title: title,
        memoryDate: memoryDate,
        location: location || null,
        content: content || null,
        displayOrder: displayOrder
    };
    
    try {
        const url = id ? `/api/memories/${id}` : '/api/memories';
        const method = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            alert('추억이 저장되었습니다! 💕');
            closeMemoryForm();
            memoriesLoaded = false;
            loadMemories();
        } else {
            alert('저장에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// 추억 수정
async function editMemory(id) {
    try {
        const response = await fetch(`/api/memories/${id}`);
        const memory = await response.json();
        
        document.getElementById('memoryId').value = memory.id;
        document.getElementById('memoryTitle').value = memory.title;
        document.getElementById('memoryDate').value = memory.memoryDate;
        document.getElementById('memoryLocation').value = memory.location || '';
        document.getElementById('memoryContent').value = memory.content || '';
        document.getElementById('memoryDisplayOrder').value = memory.displayOrder || 0;
        document.getElementById('modalTitle').textContent = '추억 수정';
        
        openMemoryForm();
    } catch (error) {
        console.error('Error:', error);
        alert('추억을 불러오는데 실패했습니다.');
    }
}

// 추억 삭제
async function deleteMemory(id) {
    if (!confirm('정말 이 추억을 삭제하시겠습니까?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/memories/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('추억이 삭제되었습니다.');
            memoriesLoaded = false;
            loadMemories();
        } else {
            alert('삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// 모달 열기
function openMemoryForm() {
    document.getElementById('memoryModal').style.display = 'block';
    document.getElementById('memoryForm').reset();
    document.getElementById('memoryId').value = '';
    document.getElementById('modalTitle').textContent = '새 추억 작성';
}

// 모달 닫기
function closeMemoryForm() {
    document.getElementById('memoryModal').style.display = 'none';
    document.getElementById('memoryForm').reset();
    document.getElementById('memoryId').value = '';
}

// 커플 정보 삭제
async function deleteCouple() {
    // 커플 정보에서 ID 가져오기
    const deleteBtn = document.querySelector('.delete-couple-btn');
    const coupleId = deleteBtn ? deleteBtn.dataset.id : null;
    
    if (!coupleId) {
        alert('커플 정보를 찾을 수 없습니다.');
        return;
    }
    
    // 확인 메시지
    if (!confirm('정말 커플 정보를 삭제하시겠습니까?\n\n⚠️ 주의: 삭제된 정보는 복구할 수 없습니다.')) {
        return;
    }
    
    // 한 번 더 확인
    if (!confirm('정말로 삭제하시겠습니까?\n모든 커플 정보가 영구적으로 삭제됩니다.')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/couple/${coupleId}`, {
            method: 'DELETE'
        });
        
        if (response.ok || response.status === 204) {
            alert('커플 정보가 삭제되었습니다.');
            location.reload();
        } else {
            alert('삭제에 실패했습니다. 다시 시도해주세요.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// XSS 방지
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 모달 외부 클릭시 닫기
window.onclick = function(event) {
    const messageModal = document.getElementById('messageModal');
    const passwordModal = document.getElementById('messagePasswordModal');
    const memoryModal = document.getElementById('memoryModal');
    const photoModal = document.getElementById('photoModal');
    
    if (event.target === messageModal) {
        closeMessageForm();
    }
    if (event.target === passwordModal) {
        closePasswordModal();
    }
    if (event.target === memoryModal) {
        closeMemoryForm();
    }
    if (event.target === photoModal) {
        closePhotoForm();
    }
}

// 사진 미리보기
function previewPhoto(input) {
    const preview = document.getElementById('photoPreview');
    const previewImage = document.getElementById('previewImage');
    
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        
        reader.onload = function(e) {
            previewImage.src = e.target.result;
            preview.style.display = 'block';
        };
        
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.style.display = 'none';
    }
}

// 사진 업로드 미리보기
function previewPhotoUpload(input) {
    const preview = document.getElementById('photoUploadPreview');
    const previewImage = document.getElementById('photoUploadImage');
    
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        
        reader.onload = function(e) {
            previewImage.src = e.target.result;
            preview.style.display = 'block';
        };
        
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.style.display = 'none';
    }
}

// 사진 목록 불러오기
async function loadPhotos() {
    if (photosLoaded) {
        return;
    }
    
    const photosList = document.getElementById('photosList');
    const loading = document.getElementById('photosLoading');
    
    loading.style.display = 'block';
    photosList.innerHTML = '';
    
    try {
        const response = await fetch('/api/photos');
        const photos = await response.json();
        
        photosLoaded = true;
        loading.style.display = 'none';
        
        if (photos.length === 0) {
            photosList.innerHTML = '<p class="no-photos">아직 사진이 없습니다. 첫 사진을 업로드해보세요! 📸</p>';
            return;
        }
        
        // displayOrder로 정렬
        photos.sort((a, b) => (a.displayOrder || 0) - (b.displayOrder || 0));
        
        photos.forEach(photo => {
            const photoCard = createPhotoCard(photo);
            photosList.appendChild(photoCard);
        });
    } catch (error) {
        console.error('사진 목록 로드 실패:', error);
        loading.style.display = 'none';
        photosList.innerHTML = '<p class="no-photos">사진을 불러오는데 실패했습니다. 다시 시도해주세요.</p>';
    }
}

// 사진 카드 생성
function createPhotoCard(photo) {
    const card = document.createElement('div');
    card.className = 'photo-card';
    card.dataset.id = photo.id;
    
    const createdAt = new Date(photo.createdAt);
    const formattedDate = createdAt.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
    
    card.innerHTML = `
        <div class="photo-image-container">
            <img src="${photo.filePath}" alt="${escapeHtml(photo.originalFileName)}" class="photo-image" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22200%22 height=%22200%22%3E%3Crect fill=%22%23ddd%22 width=%22200%22 height=%22200%22/%3E%3Ctext fill=%22%23999%22 font-family=%22sans-serif%22 font-size=%2214%22 dy=%2210.5%22 x=%2250%25%22 y=%2250%25%22 text-anchor=%22middle%22%3E이미지 로드 실패%3C/text%3E%3C/svg%3E'">
            <button class="delete-photo-btn" onclick="deletePhoto(${photo.id})" title="삭제">🗑️</button>
        </div>
        <div class="photo-info">
            <div class="photo-filename">${escapeHtml(photo.originalFileName)}</div>
            ${photo.description ? `<div class="photo-description">${escapeHtml(photo.description)}</div>` : ''}
            <div class="photo-date">📅 ${formattedDate}</div>
        </div>
    `;
    
    return card;
}

// 사진 업로드
async function uploadPhoto() {
    const fileInput = document.getElementById('photoFile');
    const description = document.getElementById('photoDescription').value;
    const displayOrder = parseInt(document.getElementById('photoDisplayOrder').value) || 0;
    
    if (!fileInput.files || fileInput.files.length === 0) {
        alert('사진을 선택해주세요.');
        return;
    }
    
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    if (description) {
        formData.append('description', description);
    }
    formData.append('displayOrder', displayOrder);
    
    try {
        const response = await fetch('/api/photos', {
            method: 'POST',
            body: formData
        });
        
        if (response.ok) {
            alert('사진이 업로드되었습니다! 📸');
            closePhotoForm();
            photosLoaded = false;
            loadPhotos();
        } else {
            alert('업로드에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// 사진 삭제
async function deletePhoto(id) {
    if (!confirm('정말 이 사진을 삭제하시겠습니까?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/photos/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok || response.status === 204) {
            alert('사진이 삭제되었습니다.');
            photosLoaded = false;
            loadPhotos();
        } else {
            alert('삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}

// 사진 모달 열기
function openPhotoForm() {
    document.getElementById('photoModal').style.display = 'block';
    document.getElementById('photoForm').reset();
    document.getElementById('photoUploadPreview').style.display = 'none';
}

// 사진 모달 닫기
function closePhotoForm() {
    document.getElementById('photoModal').style.display = 'none';
    document.getElementById('photoForm').reset();
    document.getElementById('photoUploadPreview').style.display = 'none';
}



