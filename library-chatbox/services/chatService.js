const fs = require("fs");
const path = require("path");
const axios = require("axios");
const { log } = require("console");

const GEMINI_API_KEY = process.env.GEMINI_API_KEY;

// Đọc thông tin thư viện từ file
const knowledge = fs.readFileSync(
  path.join(__dirname, "../data/library_knowledge.txt"),
  "utf8"
);

// Tạo lịch sử ban đầu với knowledge (chỉ gọi 1 lần khi bắt đầu chat)
const initHistory = async () => {
  try {
    // Gọi API backend để lấy danh sách sách
    const backendResponse = await axios.get(
      "https://gateway-service-latest-3c08.onrender.com/api/v1/book-service/books"
    );

    const books = backendResponse.data;

    // Lọc thông tin cần thiết từ dữ liệu books
    const bookInfoList = books
      .map((book, index) => {
        return `[${index + 1}] Tên: ${book.title},Mô tả: ${
          book.description
        },Tác giả: ${book.authors},Năm xuất bản: ${
          book.publishedYear
        },Thể loại: ${book.category.name}`;
      })
      .join("\n");
    console.log("Book Info List:", bookInfoList);

    //Tạo đoạn thông tin đầu vào cho AI
    const combinedInfo = `Dưới đây là thông tin thư viện:\n${knowledge}\n\nDưới đây là danh sách sách mới nhất từ hệ thống:\n${bookInfoList}`;

    return [
      {
        role: "user",
        parts: [
          {
            text: `${combinedInfo}
                  Dữ liệu trên gồm thông tin thư viện và danh sách các sách mới nhất.
                  Hãy dùng thông tin này để trả lời mọi câu hỏi từ người dùng liên quan đến thư viện, sách, tác giả, mô tả, năm xuất bản, và thể loại. Nếu không tìm thấy, hãy phản hồi một cách lịch sự.`,
          },
        ],
      },
    ];
  } catch (error) {
    console.error("Lỗi khi khởi tạo lịch sử:", error.message);
    return [
      {
        role: "user",
        parts: [
          {
            text: `Dưới đây là thông tin thư viện:\n${knowledge}\n(Gặp lỗi khi lấy dữ liệu sách từ hệ thống backend).`,
          },
        ],
      },
    ];
  }
};

// Hàm xử lý câu trả lời từ Gemini
const getResponse = async (userInput, history) => {
  try {
    const messages = [
      ...history,
      { role: "user", parts: [{ text: userInput }] },
    ];
    const geminiResponse = await axios.post(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`,
      { contents: messages },
      { headers: { "Content-Type": "application/json" } }
    );

    const answer =
      geminiResponse.data.candidates?.[0]?.content?.parts?.[0]?.text;

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
