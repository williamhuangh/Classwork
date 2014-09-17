class User < ActiveRecord::Base
  has_many :photos
  has_many :comments
  def password
    @val
  end
  def password=(val)
    @val = val
    temp = SecureRandom.hex
    self.salt = temp
    entirePass = @val + self.salt
    self.password_digest = Digest::SHA2.hexdigest(entirePass)
  end
  def password_valid?(val)
    entirePass = val + self.salt
    self.password_digest == Digest::SHA2.hexdigest(entirePass)
  end
  validates :first_name, presence: true
  validates :last_name, presence: true
  validates :login, presence: true, uniqueness: true
  validates :password, confirmation: true, presence: true
  validates :password_confirmation, presence: true
end
