class Tag < ActiveRecord::Base
	belongs_to :photo
	belongs_to :user
	validates :x_value, presence: true
	validates :y_value, presence: true
	validates :width, presence: true
	validates :height, presence: true
end
