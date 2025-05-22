const express = require("express");
const router = express.Router();
const { initHistory, getResponse } = require("../services/chatService");

let chatHistory = [];

// Gọi async để khởi tạo lịch sử trước khi xử lý request
(async () => {
  try {
    chatHistory = await initHistory();
  } catch (err) {
    console.error("Lỗi khi khởi tạo chatHistory:", err.message);
  }
})();

router.post("/", async (req, res) => {
  const { message } = req.body;

  try {
    const { answer, updatedHistory } = await getResponse(message, chatHistory);
    chatHistory = updatedHistory;

    res.json({ response: answer });
  } catch (err) {
    console.error("Lỗi trong controller:", err);
    res.status(500).json({ error: "Lỗi xử lý từ máy chủ." });
  }
});

module.exports = router;
