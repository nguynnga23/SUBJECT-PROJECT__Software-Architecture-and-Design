const fs = require("fs");
const path = require("path");
const axios = require("axios");

const GEMINI_API_KEY = process.env.GEMINI_API_KEY;

// Đọc thông tin thư viện từ file
const knowledge = fs.readFileSync(
  path.join(__dirname, "../data/library_knowledge.txt"),
  "utf8"
);

// Tạo lịch sử ban đầu với knowledge (chỉ gọi 1 lần khi bắt đầu chat)
const initHistory = () => [
  {
    role: "user",
    parts: [
      {
        text: `Dưới đây là thông tin thư viện:\n${knowledge}\nHãy sử dụng thông tin này để hỗ trợ các câu hỏi về thư viện.`,
      },
    ],
  },
];

// Hàm xử lý câu trả lời từ Gemini
const getResponse = async (userInput, history) => {
  try {
    const messages = [
      ...history,
      { role: "user", parts: [{ text: userInput }] },
    ];

    const response = await axios.post(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`,
      { contents: messages },
      { headers: { "Content-Type": "application/json" } }
    );

    const answer = response.data.candidates?.[0]?.content?.parts?.[0]?.text;

    return {
      answer: answer || "Xin lỗi, tôi chưa hiểu câu hỏi của bạn.",
      updatedHistory: [
        ...messages,
        {
          role: "model",
          parts: [{ text: answer }],
        },
      ],
    };
  } catch (error) {
    console.error(
      "Lỗi khi gọi Gemini API:",
      error.response?.data || error.message
    );
    return {
      answer: "Xin lỗi, có lỗi xảy ra khi xử lý câu hỏi.",
      updatedHistory: history,
    };
  }
};

module.exports = { initHistory, getResponse };
