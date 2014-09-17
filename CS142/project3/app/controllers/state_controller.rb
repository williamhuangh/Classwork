class StateController < ApplicationController
  @statesArray
  @substr
  def filter
    if params[:substring] != nil
      @substr = params[:substring]
      @statesArray = State.filter(@substr)
    end
  end
end
