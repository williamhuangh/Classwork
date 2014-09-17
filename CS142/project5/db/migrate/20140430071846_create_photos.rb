class CreatePhotos < ActiveRecord::Migration
  def change
    create_table :photos do |t|
      t.integer  :user_id
      t.date     :date_time
      t.string   :file_name
    end
  end
end
