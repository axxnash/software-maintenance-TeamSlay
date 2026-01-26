-- Insert sample rooms
INSERT INTO room (room_name, floor, price_per_night, max_guests, description) VALUES 
('Deluxe Suite', '3', 150.00, 2, 'Luxurious suite with king bed and city view'),
('Standard Room', '2', 80.00, 2, 'Comfortable room with queen bed'),
('Family Room', '4', 120.00, 4, 'Spacious room with two double beds'),
('Executive Suite', '5', 200.00, 2, 'Premium suite with separate living area'),
('Economy Room', '1', 60.00, 1, 'Cozy single room with basic amenities');

-- Insert sample features
INSERT INTO features (feature_name) VALUES
('WiFi'),
('Air Conditioning'),
('TV'),
('Mini Bar'),
('Safe'),
('Balcony'),
('Ocean View');

-- Link features to rooms (room_id, feature_id)
INSERT INTO room_features (room_id, feature_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2), (3, 3), (3, 4),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6), (4, 7),
(5, 1), (5, 2), (5, 3);
