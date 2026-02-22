const canvas = document.getElementById('canvas-draw');
const ctx = canvas.getContext('2d');

const colorPicker = document.getElementById('color-picker');
const clearButton = document.getElementById('button-clear');

let isDrawing = false;
let lastX = 0;
let lastY = 0;

canvas.addEventListener('mousedown', (event) => {
    isDrawing = true;
    [lastX, lastY] = [event.offsetX, event.offsetY];
    console.log(lastX, lastY);
    console.log(isDrawing);
});

canvas.addEventListener('mouseup', () => {
    isDrawing = false;
    console.log(isDrawing);
});

canvas.addEventListener('mouseout', () => {
    isDrawing = false;
    console.log(isDrawing);
});

canvas.addEventListener('mousemove', draw);

function draw(event) {
    if (!isDrawing) {
        return;
    }
    ctx.strokeStyle = colorPicker.value
    ctx.lineWidth = 3;
    ctx.lineCap = 'round';

    ctx.beginPath();
    ctx.moveTo(lastX, lastY);
    ctx.lineTo(event.offsetX, event.offsetY);
    ctx.stroke();
    [lastX, lastY] = [event.offsetX, event.offsetY];
}

clearButton.addEventListener('click', () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
});