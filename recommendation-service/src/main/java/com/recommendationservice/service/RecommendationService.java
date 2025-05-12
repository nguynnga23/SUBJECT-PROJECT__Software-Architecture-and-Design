package com.recommendationservice.service;

import jakarta.annotation.PostConstruct;
import org.tensorflow.*;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
import org.tensorflow.types.TFloat32;

public class RecommendationService {
    private SavedModelBundle model;

    // Phương thức khởi tạo mô hình khi ứng dụng bắt đầu
    @PostConstruct
    public void init() {
        // Tải mô hình đã lưu từ thư mục
        this.model = SavedModelBundle.load(
                "D:\\Nga\\Nam4\\Ki2_Nam4\\kien-truc-va-thiet-ke-phan-mem\\SUBJECT-PROJECT__Software-Architecture-and-Design\\scripts\\saved_model\\saved_model.pb",
                "serve"
        );
    }

    // Phương thức thực hiện dự đoán
    public float predict(float[] input) {
        // Tạo tensor từ mảng float[]
        try (Tensor inputTensor = TFloat32.tensorOf(Shape.of(input.length), (FloatDataBuffer) NdArrays.vectorOf(input));
             TFloat32 result = (TFloat32) model.session()
                     .runner()
                     .feed("dense_input", inputTensor) // Đưa dữ liệu đầu vào vào mô hình
                     .fetch("dense_1") // Lấy kết quả từ node "dense_1"
                     .run()
                     .get(0)) { // Truy xuất kết quả đầu tiên từ mô hình

            return result.getFloat(); // Trả về giá trị dự đoán
        }
    }
}
