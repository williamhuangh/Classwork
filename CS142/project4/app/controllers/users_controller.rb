class UsersController < ApplicationController
  def index()
    @users = User.find(:all)
  end
  def show()
    @user = User.find(params[:id])
    @photos = @user.photos
  end
end
